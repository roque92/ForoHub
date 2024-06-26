package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.example.forohub.configurations.JwtConfiguration;
import org.example.forohub.dtos.userDTO.UserLogin;
import org.example.forohub.dtos.userDTO.UserRegistration;
import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.UsersRepository;
import org.example.forohub.services.userServices.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/login")
public class LoginController {

    private UserService userService;
    private UsersRepository usersRepository;
    private JwtConfiguration jwtConfiguration;

    public LoginController(UserService userService, UsersRepository usersRepository,
            JwtConfiguration jwtConfiguration) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.jwtConfiguration = jwtConfiguration;
    }

    @PostMapping()
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin login) {

        UsersEntity user = usersRepository.findByEmail(login.email());
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }
        if (!userService.matchPassword(login.password(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }
        String token = jwtConfiguration.jwt(user.getPassword());

        
        return ResponseEntity.ok("\n" + token + "\n\n" + user.getPassword() + "\n\n");
    }

    @PostMapping("/create")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserRegistration registration) {
        var emailAlreadyExists = usersRepository.findByEmail(registration.email());
        if (emailAlreadyExists != null) {
            return ResponseEntity.badRequest().body("El correo ya existe");
        }
        UsersEntity preparedUser = userService.prepareUserForCreation(registration);
        usersRepository.save(preparedUser);
        return ResponseEntity.ok("Usuario creado exitosamente");
    }

}
