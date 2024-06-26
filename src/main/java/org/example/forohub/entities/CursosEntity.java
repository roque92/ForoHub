package org.example.forohub.entities;

import java.util.List;

import org.example.forohub.dtos.cursoDTO.CourseRegistration;
import org.example.forohub.dtos.cursoDTO.ExistingCourses;
import org.example.forohub.dtos.topicDTO.TopicRegistration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    
    private ExistingCourses cursoCategory;

    @OneToMany(mappedBy = "curso")
    private List<TopicEntity> cursosTopic;

    
    public CursosEntity convertToCursosEntity(TopicRegistration topicRegistration){
        CursosEntity cursosEntity = new CursosEntity();
        CourseRegistration courseRegistration = topicRegistration.curso();
        cursosEntity.setName(courseRegistration.name());
        cursosEntity.setCursoCategory(courseRegistration.category());
        return cursosEntity;
    }
    
    //Create
    public CursosEntity(CourseRegistration curso) {
        this.name = curso.name();
        this.cursoCategory = curso.category();
    }

    //Read

    //Update

    //Delete

}
