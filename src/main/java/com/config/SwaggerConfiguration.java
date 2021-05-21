package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Jason Wong
 * @title:
 * @projectName hzgt
 * @description: Swagger配置类.
 * @date 2021/2/28 10:13
 */

@EnableSwagger2  // Swagger的开关，表示已经启用Swagger
@Configuration      // 声明当前配置类
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerPersonApi10() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
      Contact contact=  new Contact("geoparse", "www.geoparse.com", "");
        return new ApiInfoBuilder()
                .title("空间数据解析服务")
                .description("dwg、shp、txt格式空间数据解析为geojson字符串服务")
                .version("1.0")
                .contact(contact)
                .termsOfServiceUrl("hz-parseGeo")
                .build();
    }


}

