package com.offcn.sellergoods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.dao.BrandMapper;
import com.offcn.sellergoods.pojo.Brand;
import com.offcn.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper,Brand>  implements BrandService {

    @Autowired
    BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
//        List<Brand> brandList = brandMapper.selectList(new QueryWrapper<>());
        return this.list();
    }

    @Override
    public Brand findById(Long id) {
        if(id == null || id < 0){
            // 创建自定义类型的异常   BrandException
            throw new RuntimeException("参数不正确");
        }
//        brandMapper.selectById()
        return this.getById(id);// select * from tb_brand where id = ?
    }

    /**
     * image
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        //参数校验
        if(brand==null){
            throw new RuntimeException("参数不能为空");
        }
        if(StringUtils.isEmpty(brand.getName())){
            throw new RuntimeException("品牌名称不能为空");
        }
        if(StringUtils.isEmpty(brand.getFirstChar())){
            throw new RuntimeException("品牌首字母不能为空");
        }
        if(brand.getFirstChar().length()!=1){
            throw new RuntimeException("品牌首字母长度必须为1");
        }
//        brandMapper.insert()
        this.save(brand);
    }

    @Override
    public void update(Brand brand) {
        if(brand==null){
            throw new RuntimeException("参数不能为空");
        }
        if(StringUtils.isEmpty(brand.getName())){
            throw new RuntimeException("品牌名称不能为空");
        }
        if(StringUtils.isEmpty(brand.getFirstChar())){
            throw new RuntimeException("品牌首字母不能为空");
        }
        if(brand.getFirstChar().length()!=1){
            throw new RuntimeException("品牌首字母长度必须为1");
        }
        if(StringUtils.isEmpty(brand.getId())){
            throw new RuntimeException("主键不能为空");
        }
//        brandMapper.updateById()
        this.updateById(brand);//根据id主键更新
    }

    @Override
    public void delete(Long id) {
        if(id==null || id < 0){
            throw new RuntimeException("参数不正确");
        }
//        this.delete(id);
        this.removeById(id);// delete from tb_brand where id = ?
    }

    @Override
    public List<Brand> findList(Brand brand) {

        QueryWrapper<Brand> queryWrapper = this.createQueryWrapper(brand);

//        brandMapper.selectList(queryWrapper);
        List<Brand> list = this.list(queryWrapper);

        return list;
    }

    @Override
    public PageResult<Brand> findPage(int page, int size) {
        //设置分页参数

        Page pageParam = new Page(page, size);

        IPage iPage = this.page(pageParam, new QueryWrapper<>());
//        IPage iPage2 = this.page(pageParam);

        List list = iPage.getRecords();
        long total = iPage.getTotal();

        PageResult<Brand> pageResult = new PageResult<>(total,list);

        return pageResult;
    }

    @Override
    public PageResult<Brand> findPage(Brand brand, int page, int size) {

        //设置分页参数

        Page pageParam = new Page(page, size);

        IPage iPage = this.page(pageParam,this.createQueryWrapper(brand));

        List list = iPage.getRecords();
        long total = iPage.getTotal();

        PageResult<Brand> pageResult = new PageResult<>(total,list);

        return pageResult;


    }

    @Override
    public List<Map> selectOptions() {
        return brandMapper.selectOptions();
    }


    private QueryWrapper<Brand> createQueryWrapper(Brand brand){
        //拼接查询条件
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();

        if(brand!=null){
            if(!StringUtils.isEmpty(brand.getId())){
                // where id = ?
                queryWrapper.eq("id",brand.getId());
            }
            if(!StringUtils.isEmpty(brand.getName())){
                // name like ?
                queryWrapper.like("name",brand.getName());
            }
            if(!StringUtils.isEmpty(brand.getFirstChar())){
                // first_char = ?
                queryWrapper.eq("first_char",brand.getFirstChar());
            }
        }
        return queryWrapper;
    }

}
