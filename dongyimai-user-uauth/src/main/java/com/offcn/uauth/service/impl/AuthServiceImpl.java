package com.offcn.uauth.service.impl;

import com.offcn.uauth.service.AuthService;
import com.offcn.uauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    /***
     * 授权认证方法
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //申请令牌
        AuthToken authToken = applyToken(username,password,clientId, clientSecret);
        if(authToken == null){
            throw new RuntimeException("申请令牌失败");
        }
        return authToken;
    }


    /****
     * 认证方法
     * @param username:用户登录名字
     * @param password：用户密码
     * @param clientId：配置文件中的客户端ID
     * @param clientSecret：配置文件中的秘钥
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

        //选中认证服务的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("USER-AUTH");
        if (serviceInstance == null) {
            throw new RuntimeException("找不到对应的服务");
        }
        //获取令牌的url
        String path = serviceInstance.getUri().toString() + "/oauth/token";


        //定义body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //密码模式
        body.add("grant_type", "password");
        //账号
        body.add("username", username);
        //密码
        body.add("password", password);


        //定义头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", httpbasic(clientId, clientSecret));


        //当通过RestTemplate调用服务发生异常时，往往会返回400 Bad Request或500 internal error等错误信息。
        //如果想捕捉服务本身抛出的异常信息，需要通过自行实现RestTemplate的ErrorHandler。
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        Map map = null;
        try {
            //http请求spring security的申请令牌接口
            ResponseEntity<Map> mapResponseEntity =
                    restTemplate.exchange(
                            path,
                            HttpMethod.POST,
                            new HttpEntity<MultiValueMap<String, String>>(body, header),
                            Map.class
                    );

            //获取响应数据
            map = mapResponseEntity.getBody();

        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }


        if(map == null || map.get("access_token") == null
                || map.get("refresh_token") == null || map.get("jti") == null) {
            throw new RuntimeException("创建令牌失败！");
        }

        //将响应数据封装成AuthToken对象
        AuthToken authToken = new AuthToken();

        String accessToken = (String) map.get("access_token");
        String refreshToken = (String) map.get("refresh_token");
        String jwtToken= (String) map.get("jti");

        authToken.setJti(jwtToken);
        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refreshToken);

        return authToken;
    }


    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId,String clientSecret){
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}