package com.vedruna.projectmgmt.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Project Management",       // Título de la API
        description = "API para gestionar proyectos de software para un portfolio",  // Descripción
        version = "0.1.0"                           // Versión de la API
    )
)
public class SwaggerConfig {
   //Config Extra
   // para acceder a la configuración: localhost:8080/swagger-ui.html
}
