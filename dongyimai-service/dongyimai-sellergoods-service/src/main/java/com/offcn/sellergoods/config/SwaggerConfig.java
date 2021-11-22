package com.offcn.sellergoods.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    //配置文档属性
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("商品微服务接口文档")
                .description("给前端看的")
                .version("1.0")
                .termsOfServiceUrl("http://www.dongyimai.com")
                .build();
    }

    //创建文档配置对象
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo()) //上面的私有方法
                .groupName("sellergoods")
                .select()
                .build();
    }
}
