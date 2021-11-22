package com.offcn.order.service;

import com.offcn.order.group.Cart;

import java.util.List;

public interface CartService {

    /**
     * 加入购物车，但是购物车并没有保存到redis，而是返回新的购物车数据
     * @param cartList 原来的购物车
     * @param skuId  sku商品id
     * @param num 数量
     * @return  返回新集合
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long skuId, Integer num);

    /**
     * 将购物车保存到redis中（hash）
     * @param username  （key）
     * @param cartList   （value）
     */
    public void saveCartListToRedis(String username, List<Cart> cartList);

    /**
     * 查询购物车
     * @param username
     * @return
     */
    public List<Cart> findCartListFromRedis(String username);

}
