package com.shopservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8090}")
    private int serverPort;

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public OpenAPI shopServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShopService API")
                        .description("""
                                REST API for the ShopDemo e-commerce application.

                                **Authentication**: Protected endpoints require `X-Session-Token` header.
                                Obtain a token via `POST /api/auth/login`.

                                **Environment**: `%s`
                                """.formatted(activeProfile))
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("ShopDemo")
                                .email("support@shopdemo.com"))
                        .license(new License()
                                .name("MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description(activeProfile + " server")))
                // Global security scheme — used by protected endpoints
                .addSecurityItem(new SecurityRequirement().addList("SessionToken"))
                .components(new Components()
                        .addSecuritySchemes("SessionToken",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-Session-Token")
                                        .description("Session token returned by POST /api/auth/login")));
    }
}
