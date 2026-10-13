package com.beautysalon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/", "classpath:/static/");
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/", "classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/", "classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/images/", "classpath:/static/images/");
        registry.addResourceHandler("/favicon.ico", "/favicon.svg")
                .addResourceLocations("/images/favicon.svg", "classpath:/static/favicon.svg", "classpath:/static/");
    }
}
