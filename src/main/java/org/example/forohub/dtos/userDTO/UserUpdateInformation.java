package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


public record UserUpdateInformation(
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    String password
) {

}
