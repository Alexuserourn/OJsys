package com.example.oj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot 主应用类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.oj", "controller", "service", "dao", "entity", "problem", "compile", "util", "config"})
public class OJApplication implements WebMvcConfigurer {
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(OJApplication.class, args);
    }
} 