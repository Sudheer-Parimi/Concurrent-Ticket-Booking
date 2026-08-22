package com.sudheer.ticket_booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer{
    
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000", "http:localhost:5173", "http://127.0.0.1:5500") // React, Vite, Live Server
            .allowedMethods("GET", "POST","PUT", "DELETE","OPTIONS")
            .allowedHeaders("*");
    }
}
