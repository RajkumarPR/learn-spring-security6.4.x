package com.example.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

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


    // Custom Configuration for in-memory user store
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withUsername("user")
                .password("{noop}mypass@01")
                .roles("USER")
                .build();

        UserDetails adminDetails = User.withUsername("admin")
                .password("{bcrypt}$2y$12$YnPVVUzj9ZE97xxHANVHRO3KPudi7RVjXXQgh3ttSGUa7woqquSsO") // mypass@01
                .roles("USER", "ADMIN")
                .build();

        UserDetails manager = User.withUsername("manager")
                .password("{SHA-256}0f439c8f8b72f3b19479597bf175b6219a3c2181011cd02e250e0f3fba09d5fc") // mypass@01
                .roles("USER", "MANAGER", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(userDetails, adminDetails, manager);
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
