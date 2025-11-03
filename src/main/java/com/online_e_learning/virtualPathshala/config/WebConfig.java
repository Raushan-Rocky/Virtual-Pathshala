package com.online_e_learning.virtualPathshala.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Fixed: Changed from "/api/**" to "/**" for static resources
                .allowedOrigins("http://localhost:8000", "http://127.0.0.1:8000",
                        "http://localhost:3000", "http://127.0.0.1:3000",
                        "http://localhost:8049")  // Added self-origin for static files
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources from multiple locations
        registry.addResourceHandler("/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/public/",
                        "classpath:/resources/",
                        "classpath:/META-INF/resources/",
                        "file:./uploads/"  // Added uploads directory for file serving
                );

        // For Swagger UI
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}