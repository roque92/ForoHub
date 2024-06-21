package org.example.forohub.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAlias;

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
public class topicEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonAlias("id")
    private Long id;
    @JsonAlias("title")
    private String titleTopic;
    @JsonAlias("message")
    private String bodyTopic;
    @JsonAlias("creationDate")
    private LocalDateTime topicCreationDate;
    @JsonAlias("status")
    private Boolean topicStatus;

    @JsonAlias("author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private usersEntity author_id;

    @JsonAlias("curso_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private cursosEntity curso_id;


}
