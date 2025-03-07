package com.example.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        int httpStatus = HttpStatus.UNAUTHORIZED.value();
        String httpError = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        LocalDateTime timestamp = LocalDateTime.now();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("WWW-Authenticate", "Basic realm=\"Any Realm\"");
        response.setStatus(httpStatus);

        String message = (authException != null && authException.getMessage() != null) ?
                authException.getMessage() : "Unauthorized";

        String jsonResp =
                String.format("{\"timestamp\":\"%s\",\"status\":%s,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                        timestamp, httpStatus, httpError, message, request.getServletPath());

        response.getWriter().write(jsonResp);
    }
}
