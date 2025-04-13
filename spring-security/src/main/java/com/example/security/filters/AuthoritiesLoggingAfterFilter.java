package com.example.security.filters;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class AuthoritiesLoggingAfterFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthoritiesLoggingAfterFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.info("User {} authentication successful has Authorities: {}", auth.getName(), auth.getAuthorities());
        } else
            log.error("No authentication");

        chain.doFilter(request, response);
    }
}
