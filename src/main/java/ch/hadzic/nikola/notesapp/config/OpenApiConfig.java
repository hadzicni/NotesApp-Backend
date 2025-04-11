package ch.hadzic.nikola.notesapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfig is a configuration class that sets up OpenAPI documentation for the application.
 * It includes information such as the title, version, and description of the API.
 * Additionally, it defines a security scheme for bearer token authentication.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "NotesApp API Documentation", version = "v1.0.0", description = "NotesApp API für das üK Modul 295"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    // No additional configuration is needed here.
    // The annotations above will automatically generate the OpenAPI documentation.
}