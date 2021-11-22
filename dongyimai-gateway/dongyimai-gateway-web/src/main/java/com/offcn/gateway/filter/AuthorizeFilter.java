package com.offcn.gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    private static final String AUTHORIZE_TOKEN = "Authorization";

//    @Value("${gateway.ignoreUrls}")
    String ignoreUrls="/api/user/login,/api/user/add,/api/user/sendCode";

    //过滤器执行的业务逻辑
    //获取请求中的token令牌，进行校验
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //判断如果该请求访问登录或者注册接口时，是不需要校验token，直接放行

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // http://localhost:8001/api/user/login?username=offcn123&password=offcn123?token=xxxx

        // /api/user/login   /api/brand  /api/brand
        String path = request.getURI().getPath();

//        if(path.startsWith("/api/user/login") || path.startsWith("/api/user/add") || path.startsWith("/api/user/sendCode") ){
        if(ignoreUrls.contains(path)){
            //直接放行
            Mono<Void> filter = chain.filter(exchange);
            return filter;
        }


        //获取令牌

        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        if(StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN); // url?Authorization=xxxx
            if(StringUtils.isEmpty(token)){
                // cookie中获取
                HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);

                if(cookie==null || StringUtils.isEmpty(cookie.getValue())){
                    //请求中没有携带令牌，直接拦截返回了
                    response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);// 响应状态码  405
                    return response.setComplete();
                }
                token = cookie.getValue();
            }
        }


        // 用令牌，校验令牌
        try {
//            map.put("username",user.getUsername());
//            map.put("role","admin");
//            map.put("success","success");
//            Claims claims = JwtUtil.parseJWT(token);

            // claims参数存入到请求头中,这样在资源服务中就可以获取到

//            request.mutate().header(AUTHORIZE_TOKEN,claims.toString());

            if(token.startsWith("bearer ") || token.startsWith("Bearer ")){
                request.mutate().header(AUTHORIZE_TOKEN,token);
            }else{
                request.mutate().header(AUTHORIZE_TOKEN,"Bearer " + token);
            }

            //  令牌存入header中 ---> 资源服务（自己决定验证）


            return chain.filter(exchange);

        } catch (Exception e) {
            e.printStackTrace();
            //令牌不合法
            response.setStatusCode(HttpStatus.UNAUTHORIZED);// 响应状态码  405
            return response.setComplete();
        }

    }

    // 排序
    @Override
    public int getOrder() {
        return 0;
    }


    public static void main(String[] args) {

        String urls = "/api/user/login,/api/user/add,/api/user/sendCode";
        String s = "/api/user/add1";


        System.out.println(urls.contains(s));

    }

}
