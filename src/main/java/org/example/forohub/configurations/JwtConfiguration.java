package org.example.forohub.configurations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Configuration
public class JwtConfiguration {

    public String jwt (String secret){
        String token = "";
        LocalDateTime now = LocalDateTime.now();
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            token = JWT.create()
                .withIssuer("DevRoque")
                .withExpiresAt(Date.from(now.plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
                .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .sign(algorithm);
        } catch (JWTCreationException exception){
            System.err.println("Error creating JWT: " + exception.getMessage());
        }
        return token;
    }

    public boolean jwtValidation (String token, String secret){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWT.require(algorithm)
                .withIssuer("DevRoque")
                .build()
                .verify(token);
            return true;
        } catch (Exception exception){
            return false;
        }

    }

}
