package org.example.forohub.repositories;

import org.example.forohub.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long>{

    UsersEntity findByEmail(String email);

}
