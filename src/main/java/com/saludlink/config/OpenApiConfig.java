package com.saludlink.config;
/* Configuración de Swagger para la API de SaludLink. */
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI saludLinkOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("SaludLink API")
                                .description("REST API — citas, medicamentos, autenticación JWT")
                                .version("0.1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        BEARER,
                                        new SecurityScheme()
                                                .name(BEARER)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")));
    }
}
