package org.example.forohub.component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.Setter;
import org.example.forohub.configurations.JwtConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Setter
public class JwtValidations extends OncePerRequestFilter {

    private JwtConfiguration jwtConfiguration;
    private String SECRET;

    public JwtValidations(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtConfiguration.jwtValidation(token, SECRET)) {
                    Algorithm algorithm = Algorithm.HMAC256(SECRET);
                    DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

                    String rolesClaim = decodedJWT.getClaim("roles").asString();
                    String emailClaim = decodedJWT.getClaim("email").asString();

                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rolesClaim));
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token,
                            emailClaim, authorities);
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("The Secret cannot be null");
        }
    }

}
