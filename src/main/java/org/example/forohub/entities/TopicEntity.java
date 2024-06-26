package org.example.forohub.entities;

import java.time.LocalDateTime;

import org.example.forohub.dtos.cursoDTO.CursoConsult;
import org.example.forohub.dtos.topicDTO.TopicRegistration;
import org.example.forohub.dtos.userDTO.UserConsult;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title", unique = true)
    private String titleTopic;
    @Column(name = "message", unique = true)
    private String bodyTopic;
    @Column(name = "creation_date")
    private LocalDateTime topicCreationDate;
    @Column(name = "status")
    private Boolean topicStatus;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private UsersEntity author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id")
    private CursosEntity curso;

    //Creat
    public TopicEntity(TopicRegistration topicRegistration, UsersEntity author, CursosEntity curso, Long userId, Long cursoId) {
        this.titleTopic = topicRegistration.title();
        this.bodyTopic = topicRegistration.message();
        this.author = author;
        this.curso = curso;
    }
    //Read
    public UserConsult getUserConsult(){
        return new UserConsult(this.author.getName());
        
    }

    public CursoConsult getCursoConsult(){
        return new CursoConsult(this.curso.getCursoCategory());
    }

    //Update

    //Delete

}
