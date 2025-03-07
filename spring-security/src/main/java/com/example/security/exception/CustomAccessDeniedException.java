package com.example.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;


public class CustomAccessDeniedException implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        int httpStatus = HttpStatus.FORBIDDEN.value();
        String httpError = HttpStatus.FORBIDDEN.getReasonPhrase();
        LocalDateTime timestamp = LocalDateTime.now();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("WWW-Authenticate-denied", "Basic realm=\"Any Realm\"");
        response.setStatus(httpStatus);

        String message = (accessDeniedException != null && accessDeniedException.getMessage() != null) ?
                accessDeniedException.getMessage() : "Access denied";

        String jsonResp =
                String.format("{\"timestamp\":\"%s\",\"status\":%s,\"error\":\"%s\",\"message\":\"%s\",\"path\":\"%s\"}",
                        timestamp, httpStatus, httpError, message, request.getServletPath());

        response.getWriter().write(jsonResp);

    }
}
