package com.wxj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticConfig implements WebMvcConfigurer {

    // 项目静态资源绝对路径
    @Value("${myProject.static.staticUrl}")
    private String staticUrl;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+ staticUrl + "img\\");
        registry.addResourceHandler("/user/**").addResourceLocations("file:" + staticUrl + "user\\");
    }
}

