package com.shopservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                // Explicit list — never allowedHeaders("*") in production
                .allowedHeaders(
                        "Content-Type",
                        "Accept",
                        "Origin",
                        "X-Session-Token",
                        "Authorization",
                        "X-Requested-With"
                )
                // Headers the browser JS is allowed to read from responses
                .exposedHeaders("X-Session-Token")
                .allowCredentials(true)
                // Cache pre-flight response for 1 hour — reduces OPTIONS round-trips
                .maxAge(3600);
    }
}
