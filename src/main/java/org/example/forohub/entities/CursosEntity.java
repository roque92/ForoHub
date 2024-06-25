package org.example.forohub.entities;

import java.util.List;

import org.example.forohub.dtos.cursoDTO.CourseRegistration;
import org.example.forohub.dtos.cursoDTO.ExistingCourses;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Enumerated
    private ExistingCourses cursoCategory;

    @OneToMany(mappedBy = "curso")
    private List<TopicEntity> cursosTopic;

    public CursosEntity(CourseRegistration curso) {
        this.name = curso.name();
        this.cursoCategory = curso.category();
    }

}
