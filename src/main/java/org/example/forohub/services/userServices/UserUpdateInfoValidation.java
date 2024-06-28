package org.example.forohub.services.userServices;

import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateInfoValidation {

    private UserService userService;
    private UsersRepository usersRepository;

    public UserUpdateInfoValidation(UserService userService, UsersRepository usersRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
    }

    public String passwordValidation(String newPass, String oldPass) {

        if (newPass.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacia");
        }        
        if (userService.matchPassword(newPass, oldPass)) {
        return oldPass;
        }

        return userService.passwordEncoder.encode(newPass);
        
    }

    public String emailValidation(String oldEmail, String newEmail) {
        if (newEmail.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        if(oldEmail.equals(newEmail)){
            return oldEmail;
        }
        UsersEntity emailAlreadyExists = usersRepository.findByEmail(newEmail);
        if (emailAlreadyExists != null) {
            throw new IllegalArgumentException("El email ya existe");
        }
        return newEmail;
    }

    

}
