package org.example.forohub.repositories;

import org.example.forohub.entities.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long>{

    UsersEntity findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE UsersEntity u SET u.password = :password, u.email = :email WHERE u.email = :email")
    void updateEmailAndPassword(String password, String email);

}
