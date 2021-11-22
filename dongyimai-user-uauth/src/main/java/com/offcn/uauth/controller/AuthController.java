package com.offcn.uauth.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.offcn.uauth.service.AuthService;
import com.offcn.uauth.util.AuthToken;
import com.offcn.uauth.util.Result;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/user")
public class AuthController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //客户端密码
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public Result login(String username, String password, HttpServletResponse response,String clientId,String secret) {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return new Result(false,20001,"用户名或密码为空");
        }

        try {
            AuthToken authToken = authService.login(username, password, clientId, secret);
            if(authToken!=null){

                // 写入cookie
                Cookie cookie = new Cookie("Authorization", authToken.getAccessToken());
                cookie.setPath("/");
//                cookie.setDomain("localhsot");
                response.addCookie(cookie);

                return new Result(true,2000,"登录成功，请妥善保管令牌",authToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,20001,"登录失败");
    }
}
