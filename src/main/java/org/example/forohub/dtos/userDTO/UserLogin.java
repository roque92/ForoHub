package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.NotBlank;

public record UserLogin(
    @NotBlank(message = "Email de usuarion es requerido")
    String email,
    @NotBlank(message = "Contraseña es requerida")
    String password
    
) {

}
