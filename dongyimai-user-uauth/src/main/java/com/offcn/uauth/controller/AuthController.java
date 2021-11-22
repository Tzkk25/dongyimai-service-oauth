package com.offcn.uauth.controller;

import com.offcn.entity.StatusCode;
import com.offcn.uauth.service.AuthService;
import com.offcn.uauth.util.AuthToken;
import com.offcn.uauth.util.CookieUtil;
import com.offcn.uauth.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result login(String username, String password, HttpServletResponse response) {
        if(StringUtils.isEmpty(username)){
            throw new RuntimeException("用户名不允许为空");
        }
        if(StringUtils.isEmpty(password)){
            throw new RuntimeException("密码不允许为空");
        }
        //申请令牌
        AuthToken authToken =  authService.login(username,password,clientId,clientSecret);

        //用户身份令牌
        String access_token = authToken.getAccessToken();

        //将令牌存储到cookie
        CookieUtil.addCookie(response,cookieDomain,"/","Authorization",access_token,cookieMaxAge,false);

        return new Result(true, StatusCode.OK,"登录成功！");
    }

}