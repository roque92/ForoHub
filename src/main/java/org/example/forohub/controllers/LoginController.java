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

    public LoginController(UserService userService, UsersRepository usersRepository,
            JwtConfiguration jwtConfiguration) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.jwtConfiguration = jwtConfiguration;
    }

    @PostMapping()
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLogin login) {

        Optional<UsersEntity> user = usersRepository.findByEmail(login.email());
        if (!user.isPresent()) {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }
        if (!userService.passwordEncoder.matches(login.password(), user.get().getPassword())) {
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
        UsersEntity updatedInfo = user.get();
        if (userUpdateInformation.email() != null) { 
            if (!updatedInfo.getEmail().equals(userUpdateInformation.email())) {
                // Solo verificar si el nuevo email existe si es diferente al actual
                Optional<UsersEntity> newEmail = usersRepository.findByEmail(userUpdateInformation.email());
                if (newEmail.isPresent()) {
                    return ResponseEntity.badRequest().body("El email ya existe.");                    
                } else {
                    updatedInfo.setEmail(userUpdateInformation.email());
                }
            } else {
                return ResponseEntity.badRequest().body("El Correo es el mismo.");
            }
        }
        if (userUpdateInformation.password() != null) {
            if (!userService.passwordEncoder.matches(userUpdateInformation.password(), updatedInfo.getPassword())) {
                updatedInfo.setPassword(userService.passwordEncoder.encode(userUpdateInformation.password()));                
            } else {
                return ResponseEntity.badRequest().body("La contraseña es la misma.");
            }
        }
        usersRepository.save(updatedInfo);
        return ResponseEntity.ok("Datos actualizados correctamente");

    }

    return ResponseEntity.notFound().build();


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

        return ResponseEntity.notFound().build();

    }
}
