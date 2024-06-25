package org.example.forohub.repositories;

import org.example.forohub.entities.CursosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesRepository extends JpaRepository<CursosEntity, Long>{

    CursosEntity findByName(String curso);

}
