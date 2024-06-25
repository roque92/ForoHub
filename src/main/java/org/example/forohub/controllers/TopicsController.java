package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.example.forohub.dtos.topicDTO.TopicRegistration;
import org.example.forohub.entities.CursosEntity;
import org.example.forohub.entities.TopicEntity;
import org.example.forohub.repositories.CoursesRepository;
import org.example.forohub.repositories.TopicRepository;
import org.example.forohub.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    @Autowired
    private UsersRepository userRepository;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private TopicRepository topicRepository;

    // List all
    @GetMapping("/")
    public ResponseEntity<?> listAllTopics() {
        // The JWT validation is now handled by the JwtValidations filter
        // You can directly proceed to fetch and return topics
        // ...
        return ResponseEntity.ok("List of topics...");
    }

    // Create
    @PostMapping("/")
public ResponseEntity<?> newTopic(@RequestBody @Valid TopicRegistration topic) {
    var user = userRepository.findByEmail(topic.email());
    if (user == null) {
        return ResponseEntity.badRequest().body("User not found");
    }
    CursosEntity nuevoCurso = new CursosEntity(topic.curso());
    try {
        coursesRepository.save(nuevoCurso);
    } catch (DataIntegrityViolationException e) { // Manejo de error específico
        return ResponseEntity.status(409).body("Course name already exists");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error saving course: " + e.getMessage());
    }
    TopicEntity newTopic = new TopicEntity(topic, user, nuevoCurso);
    try {
        topicRepository.save(newTopic);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error saving topic: " + e.getMessage());
    }
    
    return ResponseEntity.ok("Registro guardado");
}

    // Update

    // Delete

    // Topic Details

}
