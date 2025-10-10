package com.jao.login_auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean para el encriptado de claves (Hash y Salt).
     * BCrypt es el estándar por defecto en Spring Security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de Filtros de Seguridad: Configura las reglas de acceso a la API.
     * Deshabilita CSRF y la gestión de sesiones (típico en microservicios REST/JWT).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // Deshabilitar CSRF para APIs sin estado (usando JWT)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar políticas de sesión a STATELESS
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configurar reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // Permitir acceso sin autenticación a la ruta de login y CRUD de usuarios (temporalmente para desarrollo)
                .requestMatchers("/api/v1/auth/**", "/api/v1/users/**").permitAll() 
                // Todas las demás deben estar autenticadas (si ya tuviéramos JWT)
                .anyRequest().authenticated()
            );

        return http.build();
    }
}

