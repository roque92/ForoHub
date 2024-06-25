package org.example.forohub.validations;

import java.io.IOException;

import org.example.forohub.configurations.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidations extends OncePerRequestFilter {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtConfiguration.jwtValidation(token, "$argon2id$v=19$m=16384,t=2,p=1$Wd8s+XRCfkMq/tFpJUwXEg$gAYAg+L40rQIMtQYTq8RpbY2UitddAMbZjBQQkklcgg")) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token,
                        null, null);
                SecurityContextHolder.getContext().setAuthentication(
                        authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

}
