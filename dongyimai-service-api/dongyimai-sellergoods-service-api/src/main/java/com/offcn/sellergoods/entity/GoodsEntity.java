package com.offcn.sellergoods.entity;

import com.offcn.sellergoods.pojo.Goods;
import com.offcn.sellergoods.pojo.Item;
import com.offcn.sellergoods.pojo.GoodsDesc;

import java.io.Serializable;
import java.util.List;

/**
 * 商品的组合实体类
 */
public class GoodsEntity implements Serializable {

    private Goods goods;
    private GoodsDesc goodsDesc;

    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
