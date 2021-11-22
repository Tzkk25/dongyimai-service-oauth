package com.offcn.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/****
 * @Author:ujiuye
 * @Description:Item构建
 * @Date 2021/2/1 14:19
 *****/
// 和es中的文档对象做映射   SkuInfo skuInfo --->es （skuinfo）
@Document(indexName = "skuinfo")
public class SkuInfo implements Serializable{

	@Id
//	@Field(name = "es_id")
	private Long id;//商品id，同时也是商品编号

	//title字段存入es中，以为本方式存储并且使用ik_smart模式进行分词
	@Field(type = FieldType.Text, analyzer = "ik_smart")
	private String title;//商品标题

	private String sellPoint;//商品卖点

	@Field(type = FieldType.Double)
	private BigDecimal price;//商品价格，单位为：元

	private Integer stockCount;//

	private Integer num;//库存数量

	private String barcode;//商品条形码

	private String image;//商品图片

	private Long categoryId;//所属类目，叶子类目

	private String status;//商品状态，1-正常，2-下架，3-删除

	private Date createTime;//创建时间

	private Date updateTime;//更新时间

	private String itemSn;//

	private BigDecimal costPirce;//

	private BigDecimal marketPrice;//

	private String isDefault;//

	private Long goodsId;//

	private String sellerId;//

	private String cartThumbnail;//

	//分类名称，不需要分词，设置成固定形式
	@Field(type = FieldType.Keyword)
	private String category;//

	@Field(type = FieldType.Keyword)
	private String brand;//品牌名称

	private String spec;// {"机身内存":"16G","网络":"移动3G"}

	//规格参数（定义的一个额外的属性，在Item中没有，将字符串spec，转成json，赋值给specMap）
	private Map<String,Object> specMap;

	public Map<String, Object> getSpecMap() {
		return specMap;
	}

	public void setSpecMap(Map<String, Object> specMap) {
		this.specMap = specMap;
	}

	private String seller;//


	//get方法
	public Long getId() {
		return id;
	}

	//set方法
	public void setId(Long id) {
		this.id = id;
	}
	//get方法
	public String getTitle() {
		return title;
	}

	//set方法
	public void setTitle(String title) {
		this.title = title;
	}
	//get方法
	public String getSellPoint() {
		return sellPoint;
	}

	//set方法
	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}
	//get方法
	public BigDecimal getPrice() {
		return price;
	}

	//set方法
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	//get方法
	public Integer getStockCount() {
		return stockCount;
	}

	//set方法
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	//get方法
	public Integer getNum() {
		return num;
	}

	//set方法
	public void setNum(Integer num) {
		this.num = num;
	}
	//get方法
	public String getBarcode() {
		return barcode;
	}

	//set方法
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	//get方法
	public String getImage() {
		return image;
	}

	//set方法
	public void setImage(String image) {
		this.image = image;
	}
	//get方法
	public Long getCategoryId() {
		return categoryId;
	}

	//set方法
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	//get方法
	public String getStatus() {
		return status;
	}

	//set方法
	public void setStatus(String status) {
		this.status = status;
	}
	//get方法
	public Date getCreateTime() {
		return createTime;
	}

	//set方法
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	//get方法
	public Date getUpdateTime() {
		return updateTime;
	}

	//set方法
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	//get方法
	public String getItemSn() {
		return itemSn;
	}

	//set方法
	public void setItemSn(String itemSn) {
		this.itemSn = itemSn;
	}
	//get方法
	public BigDecimal getCostPirce() {
		return costPirce;
	}

	//set方法
	public void setCostPirce(BigDecimal costPirce) {
		this.costPirce = costPirce;
	}
	//get方法
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	//set方法
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	//get方法
	public String getIsDefault() {
		return isDefault;
	}

	//set方法
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	//get方法
	public Long getGoodsId() {
		return goodsId;
	}

	//set方法
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	//get方法
	public String getSellerId() {
		return sellerId;
	}

	//set方法
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	//get方法
	public String getCartThumbnail() {
		return cartThumbnail;
	}

	//set方法
	public void setCartThumbnail(String cartThumbnail) {
		this.cartThumbnail = cartThumbnail;
	}
	//get方法
	public String getCategory() {
		return category;
	}

	//set方法
	public void setCategory(String category) {
		this.category = category;
	}
	//get方法
	public String getBrand() {
		return brand;
	}

	//set方法
	public void setBrand(String brand) {
		this.brand = brand;
	}
	//get方法
	public String getSpec() {
		return spec;
	}

	//set方法
	public void setSpec(String spec) {
		this.spec = spec;
	}
	//get方法
	public String getSeller() {
		return seller;
	}

	//set方法
	public void setSeller(String seller) {
		this.seller = seller;
	}


}
