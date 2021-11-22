package com.offcn.order;

import com.offcn.order.config.FeignInterceptor;
import com.offcn.util.TokenDecode;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.offcn.order.dao")
@EnableFeignClients(basePackages = {"com.offcn.sellergoods.feign"})
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
    @Bean
    public TokenDecode getTokenDecode(){
        return new TokenDecode();
    }
}