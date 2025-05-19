package com.example.springboot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = "com.example.springboot.controller")
public class SwaggerConfig {
    //@Bean
    //public OpenAPI springShopOpenAPI() {
    //    return new OpenAPI()
    //            .info(new Info()
    //                    .title("宿舍管理系统接口文档")
    //                    .description("宿舍管理系统接口文档")
    //                    .version("1.0"));
    //}
    @Bean
    public Docket creatRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.springboot.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("宿舍管理系统接口文档")
                .description("宿舍管理系统接口文档")
                .termsOfServiceUrl("xxx")
                .version("1.0")
                .build();
    }
} 