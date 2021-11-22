package com.offcn.sellergoods.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/****
 * @Author:ujiuye
 * @Description:Brand构建
 * @Date 2021/2/1 14:19
 *****/
@ApiModel(description = "品牌实体类",value = "Brand")
@TableName(value="tb_brand")
public class Brand implements Serializable{

	@ApiModelProperty(value = "主键id",notes = "品牌主键的id",required = true,dataType = "Long")
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
	private Long id;//主键id

	@ApiModelProperty(value = "品牌名称",notes = "品牌的名称",required = true,dataType = "string")
    @TableField(value = "name")
	private String name;//品牌名称

	@ApiModelProperty(value = "品牌首字母",notes = "品牌的首字母",required = true,dataType = "string")
    @TableField(value = "first_char")
	private String firstChar;//品牌首字母

	@ApiModelProperty(value = "品牌图像",notes = "品牌图像",required = true,dataType = "string")
    @TableField(value = "image")
	private String image;//品牌图像

	//get方法
	public Long getId() {
		return id;
	}

	//set方法
	public void setId(Long id) {
		this.id = id;
	}
	//get方法
	public String getName() {
		return name;
	}

	//set方法
	public void setName(String name) {
		this.name = name;
	}
	//get方法
	public String getFirstChar() {
		return firstChar;
	}

	//set方法
	public void setFirstChar(String firstChar) {
		this.firstChar = firstChar;
	}
	//get方法
	public String getImage() {
		return image;
	}

	//set方法
	public void setImage(String image) {
		this.image = image;
	}


}
