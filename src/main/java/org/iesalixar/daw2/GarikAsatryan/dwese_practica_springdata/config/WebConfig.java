package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (uploadPath != null && !uploadPath.isEmpty()) {
            // Convertimos la ruta relativa "uploads" a una ruta absoluta del sistema
            // y luego a URI (esto añade automáticamente el prefijo file:/// correcto)
            Path path = Paths.get(uploadPath).toAbsolutePath().normalize();
            String resourcePath = path.toUri().toString();

            logger.info("Configurando recursos estáticos.");
            logger.info("Carpeta física: {}", path);
            logger.info("URI mapeada: {}", resourcePath);

            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations(resourcePath);
        } else {
            logger.error("La variable UPLOAD_PATH no está configurada o está vacía.");
        }
    }
}
