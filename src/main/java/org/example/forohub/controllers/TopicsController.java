package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import org.example.forohub.dtos.topicDTO.TopicRegistration;
import org.example.forohub.entities.CursosEntity;
import org.example.forohub.entities.TopicEntity;
import org.example.forohub.repositories.CoursesRepository;
import org.example.forohub.repositories.TopicRepository;
import org.example.forohub.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    private static final Logger log = LoggerFactory.getLogger(TopicsController.class);

    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private TopicRepository topicRepository;

    // List all
    @GetMapping("/")
    public ResponseEntity<?> listAllTopics() {

        return ResponseEntity.ok("List of topics...");
    }

    // Create
    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> newTopic(@RequestBody @Valid TopicRegistration topic) {
        var user = userRepository.findByEmail(topic.email().trim());
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuario no existe");
        }

        CursosEntity cursosEntity = new CursosEntity();
        cursosEntity = cursosEntity.convertToCursosEntity(topic);
        if (cursosEntity.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre del curso es requerido");
        }
        if (cursosEntity.getCursoCategory() == null) {
            return ResponseEntity.badRequest().body("La categoría del curso es requerida");
        }
        try {
            cursosEntity = coursesRepository.save(cursosEntity);
        } catch (DataAccessResourceFailureException ex) {
            log.error("Database is unavailable: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error: Database is currently unavailable. Please try again later.");
        }
        if (cursosEntity.getId() == null) {
            return ResponseEntity.badRequest().body("Error al generar ID del curso");
        }

        TopicEntity newTopic = new TopicEntity(topic, user, cursosEntity, user.getId(), cursosEntity.getId());
        newTopic.setTopicCreationDate(LocalDateTime.now());
        newTopic.setTopicStatus(true);
        List<TopicEntity> check;
        check = topicRepository.findByTitleTopic(newTopic.getTitleTopic());
        if (!check.isEmpty()) {
            return ResponseEntity.badRequest().body("El título del tema ya existe");
        }
        check = topicRepository.findByBodyTopic(newTopic.getBodyTopic());
        if (!check.isEmpty()) {
            return ResponseEntity.badRequest().body("El mensaje del tema ya existe");
        }

        try {
            newTopic = topicRepository.save(newTopic);
        } catch (DataAccessResourceFailureException ex) {
            log.error("Database is unavailable: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error: Database is currently unavailable. Please try again later.");
        }

        return ResponseEntity.ok("Registro guardado con ID: " + newTopic.getId());
    }

    // Update

    // Delete

    // Topic Details

}
