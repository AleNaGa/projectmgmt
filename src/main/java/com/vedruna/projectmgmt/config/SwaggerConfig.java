package com.vedruna.projectmgmt.config;


import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

//Configuración de Swagger para la versión 1 de la API
@Configuration
@OpenAPIDefinition(
    //Información del API REST
    info = @Info(
        title = "Project Management API",      
        description = "API para gestionar proyectos de software para un portfolio",  
        version = "0.1.0",                   
        contact = @Contact(
            name = "Alejandro Navarro",              
            email = "alejandro.navarro@a.vedrunasevillasj.es",            
            url = "https://github.com/AleNaGa/projectmgmt"        
        ),
        license = @License(
            name = "GNU General Public License v2.0",  
            url = "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html" 
        )
    )
)
//Configuración de la ruta de la versión 1
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi v1Api() {
        return GroupedOpenApi.builder()
            .group("v1") 
            .pathsToMatch("/api/v1/**") //ruta de la versión 1
            .build();
    }
}
