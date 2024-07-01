package org.example.forohub.dtos.userDTO;

import jakarta.validation.constraints.Email;

public record UserUpdateInformation(
    
    @Email
    String email,
    String password
) {

}
