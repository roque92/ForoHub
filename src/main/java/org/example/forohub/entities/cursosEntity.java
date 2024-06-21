package org.example.forohub.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.persistence.Entity;
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
public class cursosEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @JsonAlias("id")
    private Long id;
    @JsonAlias("name")
    private String cursoName;
    @JsonAlias("category")
    private String cursoCategory;

    @OneToMany(mappedBy = "curso_id")
    private List<topicEntity> cursosTopic;



    
}
