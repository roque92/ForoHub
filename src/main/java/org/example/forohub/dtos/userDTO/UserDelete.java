package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDelete(
    @NotBlank
    @Email
    String email
) {

}
