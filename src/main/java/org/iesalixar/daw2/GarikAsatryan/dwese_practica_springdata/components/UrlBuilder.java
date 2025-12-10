package org.iesalixar.daw2.GarikAsatryan.dwese_practica_springdata.components;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component("urlBuilder")
public class UrlBuilder {

    public String replaceParam(String param, String value) {
        // Obtiene la petición actual
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Construye una nueva URL basada en la actual, reemplazando solo el parámetro indicado
        return ServletUriComponentsBuilder.fromRequest(request)
                .replaceQueryParam(param, value)
                .build()
                .toUriString();
    }
}