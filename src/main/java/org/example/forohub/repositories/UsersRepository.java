package org.example.forohub.repositories;

import org.example.forohub.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotBlank;

public interface UsersRepository extends JpaRepository<UsersEntity, Long>{

    UsersEntity findByEmail(@NotBlank(message = "Email de usuarion es requerido") String email);

}
