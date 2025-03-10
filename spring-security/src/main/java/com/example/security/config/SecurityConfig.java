package com.example.security.config;


import com.example.security.exception.CustomAccessDeniedException;
import com.example.security.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedException customAccessDeniedException;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedException customAccessDeniedException) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedException = customAccessDeniedException;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // customize endpoint to be protected
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/welcome", "/register", "/error").permitAll()
                .requestMatchers("/secured").authenticated()
        );
        // configure session behavior,
        http.sessionManagement(session -> session
                .invalidSessionUrl("/login")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true));

        // Spring security provides 3 session fixation strategy
        // 1. changeSessionId - by default by spring security
        // 2. newSession - creates new session without copying attributes
        // 3. migrateSession - creates new and copy attributes from old session to new session
        /*
        http.sessionManagement(session -> session
                .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId));
        */

        // optionally use https/http only, by default it accepts traffic for http/https
        /*
        http.requiresChannel(channel -> channel.anyRequest().requiresInsecure()); // force http
        http.requiresChannel(channel -> channel.anyRequest().requiresSecure()); // force https
        */

        // disable csrf
        http.csrf(AbstractHttpConfigurer::disable);

        // enable form login
        http.formLogin(config -> config.defaultSuccessUrl("/secured")
                .failureUrl("/login?error"));

        // enable basic login
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(customAuthenticationEntryPoint));

        // enable global exception handling
        http.exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedException));
        return http.build();
    }

    // Use password encoders supported by spring security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
}
