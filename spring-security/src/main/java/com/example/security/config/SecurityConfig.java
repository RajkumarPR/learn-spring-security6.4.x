package com.example.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //1. customize endpoint to be protected
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/secured").authenticated()
                .requestMatchers("/welcome", "/error").permitAll()
        );

        //2. enable form login
        http.formLogin(Customizer.withDefaults());

        //3. enable basic login
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }


    // Custom Configuration to JdbcUserDetailsManager user store
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    // Use password encoders supported by spring security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }

    // Custom Compromised Password Checker uses HaveIBeenPwned API
    // https://api.pwnedpasswords.com/range/
    // This feature, available since Spring Security 6.3
   /* @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        // The provided password is compromised, please change your password. Since our 'password01' is compromised
        return new HaveIBeenPwnedRestApiPasswordChecker();
    } */

}
