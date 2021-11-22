package com.offcn.jwt;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestJwt {
    public static void main1(String[] args) {
        JwtBuilder builder = Jwts.builder()
                .setId("888")//给令牌设置一个唯一的id编号
                .setSubject("小白")//设置主题,可以是JSON字符串(颁发给谁的令牌)
                .setIssuedAt(new Date())//设置签发日期
                .setExpiration(new Date(System.currentTimeMillis()+60000))//设置签证过期时间
                .signWith(SignatureAlgorithm.HS256,"ujiuye")//设置加密算法类型以及秘钥字符串
                //ujiuye=秘钥,颁发和解析时都需要使用秘钥
                ;
        //给令牌添加更多的信息
        Map<String,Object> map = new HashMap<>();
        map.put("name","中公U就业");
        map.put("address","四川大文件路文星段");
        builder.addClaims(map);
        //构建并返回一个字符串
        System.out.println(builder.compact());
        //eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE2MzcxMzg2NzJ9.rf7FfKPn6ghlL6ju_LVuba_T19rabb7q6yWVuoeI3uk
    }

    public static void main(String[] args) {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE2MzcxNDAzMTUsImV4cCI6MTYzNzE0MDM3NSwiYWRkcmVzcyI6IuWbm-W3neWkp-aWh-S7tui3r-aWh-aYn-autSIsIm5hbWUiOiLkuK3lhaxV5bCx5LiaIn0.YhgaVQydLTkNvBSP-P5LOGjl1TCU8AwYgofTENCiruc";
        Jws<Claims> ujiuye = Jwts.parser().setSigningKey("ujiuye").parseClaimsJws(token);
        System.out.println(ujiuye);
        System.out.println(ujiuye.getBody());
    }
}
