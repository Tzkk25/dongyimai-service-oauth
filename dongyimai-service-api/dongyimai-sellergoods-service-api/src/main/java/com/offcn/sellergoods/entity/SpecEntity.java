package com.offcn.sellergoods.entity;


import com.offcn.sellergoods.pojo.Specification;
import com.offcn.sellergoods.pojo.SpecificationOption;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "规格复合实体类",value = "SpecEntity")
public class SpecEntity implements Serializable {
    @ApiModelProperty(value = "规格对象",required = false)
    private Specification specification;

    @ApiModelProperty(value = "规格选择对象",required = false)
    private List<SpecificationOption> optionList;

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<SpecificationOption> optionList) {
        this.optionList = optionList;
    }
}
