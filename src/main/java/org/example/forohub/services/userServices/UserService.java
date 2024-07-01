package org.example.forohub.services.userServices;

import org.example.forohub.dtos.userDTO.UserRegistration;
import org.example.forohub.entities.UsersEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public final PasswordEncoder passwordEncoder;
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UsersEntity prepareUserForCreation(UserRegistration registration){
        String encryptedPassword = passwordEncoder.encode(registration.password());
        UsersEntity newUser = new UsersEntity(registration);
        newUser.setPassword(encryptedPassword); 
        return newUser;
    }
}
