package com.example.security.config;


import com.example.security.exception.CustomAccessDeniedException;
import com.example.security.exception.CustomAuthenticationEntryPoint;
import com.example.security.handlers.CustomAuthenticationFailureHandler;
import com.example.security.handlers.CustomAuthenticationSuccessHandler;
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

    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedException customAccessDeniedException,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                          CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedException = customAccessDeniedException;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // customize endpoint to be protected
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/welcome", "/register", "/error","/login/**").permitAll()
                .requestMatchers("/secured").authenticated()
        );
        // Cross Origin Resource sharing configuration
        http.cors(cors->cors.configurationSource(new CorsConfig()));

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
        http.formLogin(config -> config
                .defaultSuccessUrl("/secured")
                .failureUrl("/login?error=true")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
        );

        // enable custom logout
        http.logout(logout -> logout
                .logoutSuccessUrl("/login?logout=true")// set logout url
                .invalidateHttpSession(true)// invalidate session
                .deleteCookies("JSESSIONID")// delete cookie
                .clearAuthentication(true)// clear authentication object
                // this is clean logout operation
        );

        // enable basic login
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(customAuthenticationEntryPoint));

        // enable global exception handling
        http.exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedException));

        // build the httpSecurity object and return
        return http.build();
    }

    // Use password encoders supported by spring security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }
}
