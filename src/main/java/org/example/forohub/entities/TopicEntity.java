package org.example.forohub.entities;

import java.time.LocalDateTime;

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
    @Column(name = "creationDate")
    private LocalDateTime topicCreationDate;
    @Column(name = "status")
    private Boolean topicStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UsersEntity author_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id")
    private CursosEntity curso_id;


}
