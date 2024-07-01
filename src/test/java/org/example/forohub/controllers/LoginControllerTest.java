package org.example.forohub.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.example.forohub.dtos.userDTO.UserDelete;
import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class LoginControllerTest {

    @Autowired
    private LoginController loginController;
    @MockBean
    private UsersRepository usersRepository;


    @Test
    void testDeleteUser() {
        // Arrange
        UserDelete userDelete = new UserDelete("jose.roque3@test.com");
        UsersEntity userEntity = new UsersEntity();
        userEntity.setId(1L);
        userEntity.setEmail("jose.roque3@test.com");
        Optional<UsersEntity> optionalUser = Optional.of(userEntity);
        Mockito.when(usersRepository.findByEmail("jose.roque3@test.com")).thenReturn(optionalUser);
        // Act
        ResponseEntity<?> response = loginController.deleteUser(userDelete);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario eliminado exitosamente", response.getBody());
        Mockito.verify(usersRepository, Mockito.times(1)).deleteById(1L);
    }
    @Test
    void deleteUser_nonExistingUser_returnsBadRequest() {
        // Arrange
        UserDelete userDelete = new UserDelete("nonexistent@example.com");
        Optional<UsersEntity> optionalUser = Optional.empty();
        Mockito.when(usersRepository.findByEmail("nonexistent@example.com")).thenReturn(optionalUser);
        // Act
        ResponseEntity<?> response = loginController.deleteUser(userDelete);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuario no existe", response.getBody());
        Mockito.verify(usersRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}
    
