package com.offcn.itempage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.offcn.sellergoods.feign") // 商品服务的feign接口所在位置
public class ItemPageApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemPageApplication.class, args);
    }
}
