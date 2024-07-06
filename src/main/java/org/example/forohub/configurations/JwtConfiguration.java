package org.example.forohub.configurations;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Configuration
@Setter
public class JwtConfiguration {

    private String EMAIL;

    public String jwt (String secret) throws JWTCreationException {
        String token;
        LocalDateTime now = LocalDateTime.now();
        Algorithm algorithm = Algorithm.HMAC256(secret);
        token = JWT.create()
            .withIssuer("DevRoque")
            .withExpiresAt(Date.from(now.plusHours(2).atZone(ZoneId.systemDefault()).toInstant()))
            .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .withClaim("email", EMAIL)
            .withClaim("roles", "ADMIN")
            .sign(algorithm);
        return token;
    }

    public boolean jwtValidation(String token, String secret) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWT.require(algorithm)
            .withIssuer("DevRoque")
            .build()
            .verify(token);
        return true;
    }
    

}
