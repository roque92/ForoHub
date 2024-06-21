package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.example.forohub.dtos.userDTO.UserRegistration;
import org.example.forohub.entities.UsersEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/login")
public class LoginController {

    // login user
   
    

    //Create user if not exists
    @SuppressWarnings("rawtypes")
    @PostMapping("/create")
    public ResponseEntity userRegistration(@RequestBody @Valid UserRegistration registration){
        var userDatos =  new UsersEntity(registration);
        System.out.println(userDatos);
        return ResponseEntity.ok("usuario creado!");
    }
    
    
    
}
