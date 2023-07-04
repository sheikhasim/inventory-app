package com.nextuple.Inventory.management.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    String appName = "Sphinx Inventory";


    @Bean
    public OpenAPI customOpenAPI(SwaggerUiConfigParameters swaggerUiConfigParameters) {
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title("Sphinx Inventory"));
    }
}
