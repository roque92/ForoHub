package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistration(
    @NotBlank(message = "El nombre es requerido")
    String name,
    @NotBlank (message = "El correo electronico es requerido")
    @Email
    String email,
    @NotBlank (message = "La contraseña es requerida")
    String password

) {

}
