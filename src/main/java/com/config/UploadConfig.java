package com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 文件上传配置
 */
@Configuration
public class UploadConfig {

    // 当前主程序的路径
    @Value("${user.dir}")
    private String location;

    // 存储路径后半段
    @Value("/data/tmp/")
    private String part;

    // 单个文件大小限制
    @Value("${spring.servlet.multipart.max-file-size}")
    private long maxFileSize;

    // 总文件的大小限制
    @Value("${spring.servlet.multipart.max-request-size}")
    private long maxRequestSize;

    /**
     * 文件上传临时路径
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 设置存储路径
        String location = this.location + this.part;
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        factory.setLocation(location);
        // 单个文件最大限制
        factory.setMaxFileSize(DataSize.of(maxFileSize, DataUnit.MEGABYTES));
        // 总上传数据总大小限制
        factory.setMaxRequestSize(DataSize.of(maxRequestSize, DataUnit.MEGABYTES));

        return factory.createMultipartConfig();
    }

    /**
     * 返回上传路径
     *
     * @return
     */
    public String getLocation() {
        String location = this.location + this.part;

        return location;
    }

}
