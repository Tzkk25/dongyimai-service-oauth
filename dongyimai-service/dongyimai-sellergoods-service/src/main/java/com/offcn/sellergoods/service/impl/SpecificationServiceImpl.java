package com.offcn.sellergoods.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.SpecificationMapper;
import com.offcn.sellergoods.dao.SpecificationOptionMapper;
import com.offcn.sellergoods.dao.TypeTemplateMapper;
import com.offcn.sellergoods.entity.SpecEntity;
import com.offcn.sellergoods.pojo.Specification;
import com.offcn.sellergoods.pojo.SpecificationOption;
import com.offcn.sellergoods.pojo.TypeTemplate;
import com.offcn.sellergoods.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Specification业务层接口实现类
 * @Date 2021/2/1 14:19
 *****/
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper,Specification> implements SpecificationService {


    @Autowired
    SpecificationOptionMapper optionMapper;

    @Autowired
    SpecificationMapper specificationMapper;

    @Autowired
    TypeTemplateMapper typeTemplateMapper;

    /**
     * Specification条件+分页查询
     * @param specification 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageResult<Specification> findPage(Specification specification, int page, int size){
        Page<Specification> mypage = new Page<>(page, size);
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        IPage<Specification> iPage = this.page(mypage, queryWrapper);
        return new PageResult<Specification>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Specification分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageResult<Specification> findPage(int page, int size){
        Page<Specification> mypage = new Page<>(page, size);
        IPage<Specification> iPage = this.page(mypage, new QueryWrapper<Specification>());

        return new PageResult<Specification>(iPage.getTotal(),iPage.getRecords());
    }

    /**
     * Specification条件查询
     * @param specification
     * @return
     */
    @Override
    public List<Specification> findList(Specification specification){
        //构建查询条件
        QueryWrapper<Specification> queryWrapper = this.createQueryWrapper(specification);
        //根据构建的条件查询数据
        return this.list(queryWrapper);
    }


    /**
     * Specification构建查询对象
     * @param specification
     * @return
     */
    public QueryWrapper<Specification> createQueryWrapper(Specification specification){
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();
        if(specification!=null){
            // 主键
            if(!StringUtils.isEmpty(specification.getId())){
                queryWrapper.eq("id",specification.getId());
            }
            // 名称
            if(!StringUtils.isEmpty(specification.getSpecName())){
                queryWrapper.eq("spec_name",specification.getSpecName());
            }
        }
        return queryWrapper;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        this.removeById(id);//删除规格对象
        //删除规格选项列表
        // delete from  tb_spec_option where spec_id = ?
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spec_id",id);
        optionMapper.delete(queryWrapper);
    }

    /**
     * 修改Specification：更新规格；删除原来的规格选项列表，再重新添加
     * @param
     */
    @Override
    public void update(SpecEntity specEntity){
//        this.updateById(specification);

        //修改规格，并且它的规格选项列表也要更新

        this.updateById(specEntity.getSpecification());//修改更新规格

        // 先把之前的规格选项列表删除，再重新添加进入

        // delete from tb_spec_option where spec_id = ?
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spec_id",specEntity.getSpecification().getId());
        optionMapper.delete(queryWrapper);

        List<SpecificationOption> optionList = specEntity.getOptionList();//新的规格选项列表
        if(!CollectionUtils.isEmpty(optionList)){
            for (SpecificationOption option : optionList) {
                option.setSpecId(specEntity.getSpecification().getId());
                optionMapper.insert(option);
            }
        }

    }

    /**
     * 增加Specification
     * @param
     */
    @Override
    public void add(SpecEntity specEntity){
//        this.save(specification);

        Specification specification = specEntity.getSpecification();
        this.save(specification);//添加规格


        List<SpecificationOption> optionList = specEntity.getOptionList();

        if(optionList!=null && optionList.size()>0){
//            boolean empty = CollectionUtils.isEmpty(optionList);
            for (SpecificationOption specificationOption : optionList) {

                specificationOption.setSpecId(specification.getId());//当前规格选项所属哪一个规格

                optionMapper.insert(specificationOption);//添加规格选项
            }
        }



    }

    /**
     * 根据ID查询Specification
     * @param id
     * @return
     */
    @Override
    public SpecEntity findById(Long id){
        Specification specification = this.getById(id);

        //规格选项列表
        // select * from tb_spec_option where spec_id = ?
        QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("spec_id",id);

        List<SpecificationOption> options = optionMapper.selectList(queryWrapper);

        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setOptionList(options);

        return specEntity;

    }

    /**
     * 查询Specification全部数据
     * @return
     */
    @Override
    public List<Specification> findAll() {
        return this.list(new QueryWrapper<Specification>());
    }

    @Override
    public List<Map> selectOptions() {
        return specificationMapper.selectOptions() ;
    }

    @Override
    public List<Map> findSpecList(Long typeTemplateId) {

        TypeTemplate typeTemplate = typeTemplateMapper.selectById(typeTemplateId);

        // [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();// 关联的规格

        List<Map> maps = JSON.parseArray(specIds, Map.class);
        if(maps!=null && maps.size()>0){
            for (Map specMap : maps) {
                Long specId = Long.parseLong(String.valueOf(specMap.get("id")));
                // 根据规格id查询规格选项列表
                QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("spec_id",specId);
                List<SpecificationOption> options = optionMapper.selectList(queryWrapper);
                specMap.put("optionList",options);// {"id":27,"text":"网络"}   {"id":27,"text":"网络",optionList:[{},{}]}
            }
        }


        return maps;
    }
}
