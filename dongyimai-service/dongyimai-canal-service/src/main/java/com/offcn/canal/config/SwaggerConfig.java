package com.offcn.canal.config;

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
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("商品微服务接口文档")
                .description("前端技术人员用于做数据交互")
                .version("1.0")
                .termsOfServiceUrl("http://www.dongyimai.com")
                .build();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .groupName("dongyimai")
                .select()
                .build();
    }
}
