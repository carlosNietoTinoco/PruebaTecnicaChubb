package com.chubbTest.customer.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@Profile("!test")
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                //401
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                )
                //403
                .accessDeniedHandler((request, response, accessDeniedException) -> 
                    response.sendError(HttpServletResponse.SC_FORBIDDEN)
                )
            )
            .authorizeHttpRequests(auth -> auth
                // Permisos para la consola H2 y documentación de la API (si la hubiera)
                .requestMatchers("/h2-console/**").permitAll()
                //.requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll() // Descomentar si usas Swagger/OpenAPI

                // 1.1 Endpoint: Crear un Nuevo Cliente
                .requestMatchers(HttpMethod.POST, "/api/v1/customers").hasAuthority("SCOPE_customers:write")

                // 1.2 Endpoint: Actualizar Cliente
                .requestMatchers(HttpMethod.PATCH, "/api/v1/customers/{customerId}").hasAuthority("SCOPE_customers:update")

                // 1.3 Endpoint: Desactivar Cliente
                .requestMatchers(HttpMethod.PATCH, "/api/v1/customers/{customerId}/deactivate").hasAuthority("SCOPE_customers:deactivate")

                // 1.4 Endpoint: Activar Cliente
                .requestMatchers(HttpMethod.PATCH, "/api/v1/customers/{customerId}/activate").hasAuthority("SCOPE_customers:activate")

                .anyRequest().authenticated()
            );

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}