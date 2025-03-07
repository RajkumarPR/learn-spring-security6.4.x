package com.example.security.config;


import com.example.security.exception.CustomAccessDeniedException;
import com.example.security.exception.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //1. customize endpoint to be protected
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/welcome", "/register", "/error").permitAll()
                .requestMatchers("/secured").authenticated()
        );
        // optionally use https/http only, by default it accepts traffic for http/https
        //http.requiresChannel(channel -> channel.anyRequest().requiresInsecure()); // force http
        //http.requiresChannel(channel -> channel.anyRequest().requiresSecure()); // force https

        //2. disable csrf
        http.csrf(AbstractHttpConfigurer::disable);

        //3. enable form login
        http.formLogin(Customizer.withDefaults());

        //4. enable basic login
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(customAuthenticationEntryPoint));

        //5. enable global exception handling
        http.exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler()));
        return http.build();
    }

    // Use password encoders supported by spring security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedException();
    }
}
