package com.example.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            header = header.trim();
            if (header.startsWith("Basic ")) {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                try {
                    byte[] decoded = Base64.getDecoder().decode(base64Token);
                    String token = new String(decoded, StandardCharsets.UTF_8);
                    int deli = token.indexOf(":");
                    if (deli == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }
                    String username = token.substring(0, deli);
                    if (username.contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }

                } catch (Exception ex) {
                    throw new BadCredentialsException("Invalid basic authentication token");
                }
            }

        }
        chain.doFilter(request, response);
    }
}
