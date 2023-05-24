package com.example.springbootrabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置文件
 * @author： tks
 * @date： 2022/7/21
 */
@Configuration
@EnableWebMvc
@EnableSwagger2 // 开启
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("webApi").apiInfo(webApiInfo()).select().build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("rabbitmq接口文档")
                .description("本文档描述了rabbitmq微服务接口定义")
                .version("1.0")
                .contact(new Contact("tanglaoer", "http://tanglaoer.gitee.io", "1585701160@ttt.com"))
                .build();
    }
}
