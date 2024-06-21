package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/login")
public class loginController {

    @PostMapping("/")
    public ResponseEntity<String> loginUser(){
        return new ResponseEntity<String>("Login Successful", HttpStatus.OK);
    }
    
    
    
}
