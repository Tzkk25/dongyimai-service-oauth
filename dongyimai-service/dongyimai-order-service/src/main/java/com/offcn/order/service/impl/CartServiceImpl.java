package com.offcn.order.service.impl;


import com.offcn.entity.Result;
import com.offcn.order.group.Cart;
import com.offcn.order.pojo.OrderItem;
import com.offcn.order.service.CartService;

import com.offcn.sellergoods.feign.GoodsFeign;
import com.offcn.sellergoods.feign.ItemFeign;
import com.offcn.sellergoods.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//  cartList
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ItemFeign itemFeign;

    @Autowired
    GoodsFeign goodsFeign;

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long skuId, Integer num) {
        if(cartList==null){
            cartList = new ArrayList<>();
        }

        Result<Item> result = itemFeign.findById(skuId);

        if(result==null){
            throw new RuntimeException("商品服务调用失败（根据skuid查询商品）");
        }

        if(result.getData()==null){
            throw new RuntimeException("未查询到该商品");
        }

        Item item = result.getData();

        String sellerId = item.getSellerId();//商家的编号

        Cart cart = searchCartFromCartList(cartList,sellerId);

        if(cart == null){
            cart = new Cart();

            cart.setSellerId(sellerId);//商家id
            cart.setSellerName(item.getSeller());//商家名称
            List<OrderItem> orderItemList = new ArrayList<>();

            OrderItem orderItem = this.createOrderItem(item, num);

            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);

            cartList.add(cart);
        }else{
            List<OrderItem> orderItemList = cart.getOrderItemList();

            OrderItem orderItem = searchOrderItem(orderItemList,skuId);

            if(orderItem==null){
                orderItem = this.createOrderItem(item,num);
                orderItemList.add(orderItem);
            }else{
                orderItem.setNum(orderItem.getNum() + num);

                // num  +  -

                if(orderItem.getNum()<=0){
                    orderItemList.remove(orderItem);
                }

                if(orderItemList.size()==0){
                    cartList.remove(cart);
                }

                orderItem.setTotalFee(new BigDecimal(orderItem.getNum()*orderItem.getPrice().doubleValue()));
            }

        }


        return cartList;
    }

    private OrderItem createOrderItem(Item item,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(item.getId());//skuid
        orderItem.setPicPath(item.getImage());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(item.getPrice());
        orderItem.setNum(num);
        orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
        return orderItem;
    }

    private OrderItem searchOrderItem(List<OrderItem> orderItemList,Long skuId) {

        for (OrderItem orderItem : orderItemList) {
            if(orderItem.getItemId().longValue() == skuId.longValue()){
                return orderItem;
            }
        }

        return null;
    }

    private Cart searchCartFromCartList(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if(cart.getSellerId().equals(sellerId)){
                return cart;
            }
        }
        return null;
    }

    @Override
    public void saveCartListToRedis(String username, List<Cart> cartList) {
        redisTemplate.boundHashOps("cartList").put(username,cartList);
    }

    @Override
    public List<Cart> findCartListFromRedis(String username) {
        Object cartList = redisTemplate.boundHashOps("cartList").get(username);

        if(cartList==null){
            return new ArrayList<>();
        }

        return (List<Cart>)cartList;
    }
}
