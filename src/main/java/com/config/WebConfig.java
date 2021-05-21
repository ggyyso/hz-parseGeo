package com.config;


import com.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

// 如果Swagger不能访问，则使用WebMvcConfigurerAdapter
@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
public class WebConfig extends WebMvcConfigurationSupport {
    @Bean
    public LoginInterceptor authInterceptor() {
        return new LoginInterceptor();
    }

    // 指定资源不需要拦截
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").setCachePeriod(0);
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(0);
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(0);
        registry.addResourceHandler("/image/**")
                .addResourceLocations("classpath:/static/image/")
                .setCachePeriod(0);
        registry.addResourceHandler("**.html").
                addResourceLocations("classpath:/static/");

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(this.authInterceptor())
                .addPathPatterns("/api/bus/**", "/api/mgr/**",
                        "/api/data/**", "/api/statis/**", "/api/sys/**",
                        "/api/item/**", "/api/super/**")
                // 本地调试
                .excludePathPatterns("/api/**");
                // 服务器部署
//                .excludePathPatterns("/api/sys/user/login", "/api/sys/user/logout",
//                        "/api/obj/nearby", "/api/obj/ent",
//                        "/api/super/search", "/api/licence/unique",
//                        "/swagger-resources/**", "/webjars/**",
//                        "/v2/**", "/swagger-ui.html/**",
//                        "/configuration/**", "/error**", "/logout", "/static/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins("http://10.61.128.113", "http://localhost")
                .allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false).favorPathExtension(false);
    }
}

