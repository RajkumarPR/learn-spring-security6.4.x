package com.example.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
public class CorsConfig implements CorsConfigurationSource {
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest req) {

        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        cors.setAllowedMethods(Collections.singletonList("OPTIONS,GET,POST,PUT,DELETE"));
        cors.setAllowedHeaders(Collections.singletonList("*"));
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);

        return cors;
    }
}
