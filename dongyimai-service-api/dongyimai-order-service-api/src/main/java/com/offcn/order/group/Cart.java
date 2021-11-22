package com.offcn.order.group;



import com.offcn.order.pojo.OrderItem;

import java.io.Serializable;
import java.util.List;

// 购物车中一个商家对应一个Cart
public class Cart implements Serializable {

    private String sellerId;
    private String sellerName;
    private List<OrderItem> orderItemList;

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
