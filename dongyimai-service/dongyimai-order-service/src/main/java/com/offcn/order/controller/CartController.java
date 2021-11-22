package com.offcn.order.controller;

import com.offcn.entity.Result;
import com.offcn.entity.StatusCode;
import com.offcn.order.group.Cart;
import com.offcn.order.service.CartService;
import com.offcn.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

//    @RequestMapping("/findCartList")
//    public List<Cart> findCartList(String username) {
//        return cartService.findCartListFromRedis(username);//从redis中提取
//    }
//
//    @RequestMapping("/addGoodsToCartList")
//    public Result addGoodsToCartList(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num) {
//
//        try {
//            String username = "dongyimai" +
//                    "" +
//                    "";//测试，  jwt令牌中解析用户名     网关---》购物车服务（oauth）----》商品服务（oauht）
//            List<Cart> cartList = cartService.findCartListFromRedis(username);
//            if(cartList==null){
//                cartList = new ArrayList<>();
//            }
//            cartList = cartService.addGoodsToCartList(cartList, skuId, num);
//            cartService.saveCartListToRedis(username,cartList);
//
//            return new Result(true,StatusCode.OK,"加入购物车成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false,StatusCode.ERROR,"加入购物车失败，"+e.getMessage());
//        }
//    }

    /**
     * 购物车列表
     *
     * @return
     */
    @RequestMapping("/findCartList")
    public List<Cart> findCartList() {

        Map<String, String> userInfo = tokenDecode.getUserInfo();

        String username=userInfo.get("user_name");

        return cartService.findCartListFromRedis(username);//从redis中提取
    }


    @RequestMapping("/addGoodsToCartList")
    public Result addGoodsToCartList(Long skuId, Integer num) {

        //String username = "ujiuye";
        Map<String, String> userInfo = tokenDecode.getUserInfo();

        String username=userInfo.get("user_name");

        try {
            List<Cart> cartList = findCartList();//获取购物车列表
            cartList = cartService.addGoodsToCartList(cartList, skuId, num);
            cartService.saveCartListToRedis(username, cartList);
            return new Result(true, StatusCode.OK, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "添加失败");
        }
    }
}
