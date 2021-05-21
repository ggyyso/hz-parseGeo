package com.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class APIConfig {
    @Bean
    public Docket secDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统功能接口")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(regex("/api/sys/.*"))
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket testDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("辅助决策接口")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(Predicates.or(
                        PathSelectors.regex("/api/approval/.*"),
                        PathSelectors.regex("/api/item/.*"),
                        PathSelectors.regex("/api/obj/.*"),
                        PathSelectors.regex("/api/super/.*"),
                        PathSelectors.regex("/api/licence/.*"),
                        PathSelectors.regex("/api/statis/.*"),
                        PathSelectors.regex("/api/blacklist/.*"),
                        PathSelectors.regex("/api/bus/.*")
                ))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("sxgis",
                "http://www.sxgis.cn",
                "398644981@qq.com");
        return new ApiInfoBuilder()
                //页面标题
                .title("汉中国土资源公共接口")
                //版权
                .license("陕西省基础地理信息中心")
                .licenseUrl("http://www.sxgis.cn")
                //创建人
                .contact(contact)
                //版本号
                .version("1.0")
                //描述
                .description("服务端接口")
                .build();
    }
}
