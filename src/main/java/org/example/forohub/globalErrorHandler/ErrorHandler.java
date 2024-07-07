package org.example.forohub.globalErrorHandler;

import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> missingDataError(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        String formattedErrors = String.join(", ", errors);
        return ResponseEntity.badRequest().body("Datos incompletos: " + formattedErrors);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> missingCategory(JsonMappingException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());        
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> jwtVerificationException (JWTVerificationException  ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<?> jwtCreationException(JWTCreationException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(RuntimeException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpiredException(TokenExpiredException ex){
        return ResponseEntity.badRequest().body(ex.getMessage() + " " + ex.getExpiredOn().toString());
    }

}
