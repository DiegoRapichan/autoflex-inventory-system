package com.autoflex.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Autoflex Inventory API")
                        .version("1.0")
                        .description("REST API for Inventory Management with Production Suggestions")
                        .contact(new Contact()
                                .name("Diego Rapichan")
                                .email("your@email.com")));
    }
}
