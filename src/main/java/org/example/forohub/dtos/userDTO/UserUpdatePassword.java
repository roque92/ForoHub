package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdatePassword(
    @NotBlank(message = "Email es necesario para cambiar la contraseña")
    @Email
    String email,
    @NotBlank(message = "La contraseña no puede esta vacia")
    String password
) {

}
