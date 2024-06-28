package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import org.example.forohub.configurations.JwtConfiguration;
import org.example.forohub.dtos.userDTO.UserDelete;
import org.example.forohub.dtos.userDTO.UserLogin;
import org.example.forohub.dtos.userDTO.UserRegistration;
import org.example.forohub.dtos.userDTO.UserUpdateInformation;
import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.UsersRepository;
import org.example.forohub.services.userServices.UserService;
import org.example.forohub.services.userServices.UserUpdateInfoValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/login")
public class LoginController {

    private UserService userService;
    private UsersRepository usersRepository;
    private JwtConfiguration jwtConfiguration;
    private UserUpdateInfoValidation validation;

    public LoginController(UserService userService, UsersRepository usersRepository,
            JwtConfiguration jwtConfiguration, UserUpdateInfoValidation validation) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.jwtConfiguration = jwtConfiguration;
        this.validation = validation;
    }

    @PostMapping()
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin login) {

        Optional<UsersEntity> user = usersRepository.findByEmail(login.email());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }
        if (!userService.matchPassword(login.password(), user.get().getPassword())) {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }
        String token = jwtConfiguration.jwt(user.get().getPassword());

        return ResponseEntity.ok("\n" + token + "\n\n" + user.get().getPassword() + "\n\n");
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserRegistration registration) {
        Optional<UsersEntity> emailAlreadyExists = usersRepository.findByEmail(registration.email());
        if (emailAlreadyExists.isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya existe");
        }
        UsersEntity preparedUser = userService.prepareUserForCreation(registration);
        usersRepository.save(preparedUser);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

    // update UserInformation
    @PutMapping("/{email}")
    @Transactional
    public ResponseEntity<?> updateUserInformation(@PathVariable("email") String email,
            @RequestBody UserUpdateInformation userUpdateInformation) {
        Optional<UsersEntity> user = usersRepository.findByEmail(email);

        if (user.isPresent()) {

            String passwordValidated = validation.passwordValidation(userUpdateInformation.password(),
                    user.get().getPassword());
            String emailValidated = validation.emailValidation(user.get().getEmail(), userUpdateInformation.email());
            if (passwordValidated.equals(user.get().getPassword()) && emailValidated.equals(user.get().getEmail())) {
                return ResponseEntity.ok("Usuario y contraseña son iguales");
            }
            user.get().setPassword(passwordValidated);
            user.get().setEmail(emailValidated);
            usersRepository.updateEmailAndPassword(user.get().getPassword(), user.get().getEmail());
            return ResponseEntity.ok("Informacion actualizada correctamente");

        } else {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }

    }

    // Delete
    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestBody @Valid UserDelete userDelete) {

        Optional<UsersEntity> user = usersRepository.findByEmail(userDelete.email());
        if (user.isPresent()) {
            usersRepository.deleteById(user.get().getId());
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        }
        
        return ResponseEntity.badRequest().body("Usuario no existe");
        
    }
}
