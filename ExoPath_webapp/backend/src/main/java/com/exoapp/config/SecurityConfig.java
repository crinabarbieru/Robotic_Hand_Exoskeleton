package com.exoapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // Disable CSRF for API requests
                .csrf(csrf -> csrf.disable())
                // Configure endpoint access rules
                .authorizeRequests(authz -> authz
                        // Public endpoints (login, error, etc.)
                        .requestMatchers(
                                "/api/login",
                                "/api/users",
                                "/api/patients/**",
                                "/api/error",
                                "/api/supervisor/**",
                                "/api/auth/change-password",
                                "/error",
                                "/favicon.ico",
                                "/login"
                        ).permitAll()

                        // Swagger UI & OpenAPI Docs (Permit All)
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

