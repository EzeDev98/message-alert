package com.wm.notification.config;

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

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class SwaggerCustomConfig {

    @Value("${base-url}")
    private String baseUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Notification Management App")
                        .description("Restful APIs for Notifications")
                        .version("1.0").contact(new Contact().name("NDF")
                                .email("softwareteam.ndf@gmail.com").url("www.thewealthmarket.com"))
                        .license(new License().name("License of API")
                                .url("API license URL")))
                .servers(Collections.singletonList(new Server()
                        .url(baseUrl)
                        .description("Server")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication", Arrays.asList("read", "write")));
    }
}