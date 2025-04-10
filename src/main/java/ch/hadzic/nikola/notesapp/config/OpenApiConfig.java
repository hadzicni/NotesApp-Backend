package ch.hadzic.nikola.notesapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "NotesApp API Documentation", version = "v1.0.0", description = "NotesApp API für das üK Modul 295"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    // This class is used to configure OpenAPI documentation for the application.
    // The @OpenAPIDefinition annotation is used to define the API information such as title, version, and description.
    // The @SecurityScheme annotation is used to define the security scheme for the API, in this case, a bearer token authentication scheme.
    // The class is annotated with @Configuration to indicate that it is a Spring configuration class.
}