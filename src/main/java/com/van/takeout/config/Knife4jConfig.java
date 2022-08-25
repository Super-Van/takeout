package com.van.takeout.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableKnife4j
@EnableSwagger2
public class Knife4jConfig extends WebMvcConfigurationSupport {
    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        //指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("菩提阁外卖接口文档")
                        .termsOfServiceUrl("")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("用户服务")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.van.takeout.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
