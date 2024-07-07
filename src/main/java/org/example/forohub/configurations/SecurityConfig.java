package org.example.forohub.configurations;

import org.example.forohub.component.JwtValidations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtValidations jwtValidations)
            throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Set permissions to endpoints
                .authorizeHttpRequests(auth -> auth
                        // public enpoints
                        //Login
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        //Create Account
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()
                        //Swagger UI
                        .requestMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                // Add JwtValidations filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtValidations, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}


