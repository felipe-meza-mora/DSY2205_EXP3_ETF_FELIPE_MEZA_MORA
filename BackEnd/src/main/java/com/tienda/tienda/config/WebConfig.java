package com.tienda.tienda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Permite solicitudes CORS de todos los orígenes
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200")  // Aquí debes poner la URL de tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos que deseas permitir
                .allowedHeaders("*");  // Puedes ajustar si necesitas especificar qué headers permitir
    }
}