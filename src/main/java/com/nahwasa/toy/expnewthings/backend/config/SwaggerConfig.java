package com.nahwasa.toy.expnewthings.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SwaggerConfig {
    @Bean
    public OpenAPI initOpenApi() {
        Info info = new Info().title("Toy API").version("1.0.0")
                .description("Toy Project - Exp New Things")
                .contact(new Contact().name("nahwasa").url("https://nahwasa.com").email("nahwasa@gmail.com"));

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");

        List<SecurityRequirement> security = new ArrayList<>();
        SecurityRequirement schemaRequirement = new SecurityRequirement().addList("bearerAuth");
        security.add(schemaRequirement);

        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080").description("toy backend"));

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(schemaRequirement)
                .security(security)
                .info(info)
                .servers(servers);
    }
}
