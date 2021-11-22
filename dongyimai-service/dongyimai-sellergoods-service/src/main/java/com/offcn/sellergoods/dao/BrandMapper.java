package com.offcn.sellergoods.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.offcn.sellergoods.pojo.Brand;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends BaseMapper<Brand> {
    /**
     * 用来查询品牌列表,给前端页面添加模板弹出框的一个关联品牌提示
     *
     * @return
     */
    @Select("select id,name as text from tb_brand")
    List<Map> selectOptions();

}
