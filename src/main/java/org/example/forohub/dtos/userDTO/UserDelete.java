package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDelete(
    @NotBlank(message = "El correo es necesario para borrar el usuario")
    @Email
    String email
) {

}
