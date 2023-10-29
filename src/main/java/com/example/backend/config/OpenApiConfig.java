package com.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "backend",
                        email = "bashirokala@hotmail.com",
                        url = "https://warl0ck1111.io"),
                description = "OpenApi documentation for Social Media API",
                title = "OpenApi specification - Social Media API",
                version = "1.0",
                license = @License(
                        name = "All Rights Reserved",
                        url = "https://warl0ck1111.io"
                ),
                termsOfService = "Terms of service..."
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:9999"
                ),


        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
