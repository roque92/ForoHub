package org.example.forohub.repositories;

import org.example.forohub.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long>{

}
