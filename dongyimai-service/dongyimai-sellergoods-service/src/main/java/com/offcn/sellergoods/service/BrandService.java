package com.offcn.sellergoods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.offcn.entity.PageResult;
import com.offcn.sellergoods.pojo.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService extends IService<Brand> {

    /**
     * 查询所有品牌列表
     * @return
     */
    List<Brand> findAll();

    /**
     * 根据id查询品牌对象
     * @param id
     * @return
     */
    Brand findById(Long id);

    /**
     * 添加品牌
     * @param brand
     */
    void add(Brand brand);

    /**
     * 修改品牌
     * @param brand
     */
    void update(Brand brand);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Long id);

    /**
     * 条件查询
     * @param brand （查询的条件，id，name，firstChar）
     * @return
     */
    List<Brand> findList(Brand brand);


    /**
     * 分页查询
     * @param page 页码
     * @param size 页容量
     * @return
     */
    PageResult<Brand> findPage(int page , int size);

    /**
     * 带条件的分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageResult<Brand> findPage(Brand brand , int page , int size);


    /**
     * 查询品牌列表
     * @return
     */
    List<Map> selectOptions();

}
