package org.example.forohub.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.forohub.dtos.cursoDTO.CursoUpdateInfo;
import org.example.forohub.dtos.topicDTO.TopicConsult;
import org.example.forohub.dtos.topicDTO.TopicDelete;
import org.example.forohub.dtos.topicDTO.TopicRegistration;
import org.example.forohub.dtos.topicDTO.TopicUpdateInfo;
import org.example.forohub.entities.CursosEntity;
import org.example.forohub.entities.TopicEntity;
import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.CoursesRepository;
import org.example.forohub.repositories.TopicRepository;
import org.example.forohub.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    private static final Logger log = LoggerFactory.getLogger(TopicsController.class);

    private final UsersRepository userRepository;
    private final CoursesRepository coursesRepository;
    private final TopicRepository topicRepository;

    public TopicsController(UsersRepository userRepository, CoursesRepository coursesRepository,
                            TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.topicRepository = topicRepository;
    }



    // findAll
    @GetMapping()
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Page<TopicConsult>> listAllTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("topicCreationDate").descending());
        Page<TopicEntity> topics = topicRepository.findAll(pageable);
        if (topics.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TopicConsult> allTopics = topics.stream()
                .map(t -> new TopicConsult(
                        t.getId(),
                        t.getTitleTopic(),
                        t.getBodyTopic(),
                        t.getTopicStatus(),
                        t.getTopicCreationDate(),
                        t.getUserConsult().name(),
                        t.getCursoConsult().category()))
                .collect(Collectors.toList());

        Page<TopicConsult> resultPage = new PageImpl<>(allTopics, pageable, topics.getTotalElements());
        return ResponseEntity.ok(resultPage);
    }

    // findByID
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<TopicConsult> getTopicById(@PathVariable("id") Long id) {
        Optional<TopicEntity> topic = topicRepository.findById(id);
        if (topic.isPresent()) {
            TopicEntity topicEntity = topic.get();
            TopicConsult topicConsult = new TopicConsult(
                    topicEntity.getId(),
                    topicEntity.getTitleTopic(),
                    topicEntity.getBodyTopic(),
                    topicEntity.getTopicStatus(),
                    topicEntity.getTopicCreationDate(),
                    topicEntity.getUserConsult().name(),
                    topicEntity.getCursoConsult().category());
            return ResponseEntity.ok(topicConsult);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create
    @PostMapping()
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> createTopic(@RequestBody @Valid TopicRegistration topic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = (String) authentication.getCredentials();

        Optional<UsersEntity> user = userRepository.findByEmail(authenticatedEmail);
        if (user.isPresent()) {

            CursosEntity cursosEntity = new CursosEntity();
            cursosEntity = cursosEntity.createFromTopicRegistration(topic);
            if (cursosEntity.getName().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre del curso es requerido");
            }
            if (cursosEntity.getCursoCategory() == null) {
                return ResponseEntity.badRequest().body("La categoría del curso es requerida");
            }

            TopicEntity newTopic = new TopicEntity(topic, user.get(), cursosEntity, user.get().getId(),
                    cursosEntity.getId());
            newTopic.setTopicCreationDate(LocalDateTime.now());
            newTopic.setTopicStatus(true);
            List<TopicEntity> check;
            check = topicRepository.findByTitleTopic(newTopic.getTitleTopic());
            if (!check.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El título del tema ya existe, busca el id del tema: " + check.getFirst().getId());
            }
            check = topicRepository.findByBodyTopic(newTopic.getBodyTopic());
            if (!check.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El mensaje del tema ya existe, busca el id: " + check.getFirst().getId());
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
            newTopic.setCurso(cursosEntity);

            try {
                newTopic = topicRepository.save(newTopic);
            } catch (DataAccessResourceFailureException ex) {
                log.error("Database is unavailable: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Error: Database is currently unavailable. Please try again later.");
            }

            return ResponseEntity.ok("Registro guardado con ID: " + newTopic.getId());

        }
        return ResponseEntity.badRequest().body("Usuario no existe");
    }

    // Update
    @PutMapping("/{id}")
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> updateTopic(@PathVariable("id") Long id,
            @RequestBody TopicUpdateInfo topicUpdateInfo) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = (String) authentication.getCredentials();

        var user = userRepository.findByEmail(authenticatedEmail);

        if (user.isPresent()) {
            Optional<TopicEntity> existTopic = topicRepository.findByIdAndAuthorId(id, user.get().getId());

            if (existTopic.isPresent()) {
                TopicEntity currentTopic = existTopic.get();

                if (topicUpdateInfo.title() != null && !topicUpdateInfo.title().isEmpty()) {
                    if (topicUpdateInfo.title().equalsIgnoreCase(currentTopic.getTitleTopic())) {
                        return ResponseEntity.badRequest().body("El titulo del topic ya existe.");
                    }
                    currentTopic.setTitleTopic(topicUpdateInfo.title());
                }

                if (topicUpdateInfo.message() != null && !topicUpdateInfo.message().isEmpty()) {
                    if (currentTopic.getBodyTopic().equals(topicUpdateInfo.message())) {
                        return ResponseEntity.badRequest().body("El mensaje del topic ya existe.");
                    }
                    currentTopic.setBodyTopic(topicUpdateInfo.message());
                }


                if (currentTopic.getTopicStatus() != topicUpdateInfo.status()) {
                    currentTopic.setTopicStatus(topicUpdateInfo.status());
                }


                if (topicUpdateInfo.curso() != null) {
                    CursoUpdateInfo cursoUpdateInfo = topicUpdateInfo.curso();

                    if (cursoUpdateInfo.name() != null && !cursoUpdateInfo.name().isEmpty()) {
                        currentTopic.getCurso().setName(cursoUpdateInfo.name());
                    }

                    if (cursoUpdateInfo.category() != null && !cursoUpdateInfo.category().equals("")) {
                        currentTopic.getCurso().setCursoCategory(cursoUpdateInfo.category());
                    }
                }

                topicRepository.save(currentTopic);
                coursesRepository.save(currentTopic.getCurso());
                return ResponseEntity.ok().body("Datos actualizados correctamente");

            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("Usuario no encontrado");
    }


    @GetMapping("userTopic/{email}")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<List<TopicConsult>> topicsByEmail(@PathVariable("email") String email) {
        Optional<UsersEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            List<TopicEntity> topics = topicRepository.findAllByAuthorId(user.get().getId());
            if (topics.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<TopicConsult> topicsByEmail = topics.stream()
            .map(t -> new TopicConsult(
                    t.getId(),
            t.getTitleTopic(),
            t.getBodyTopic(),
            t.getTopicStatus(),
            t.getTopicCreationDate(),
            t.getUserConsult().name(),
            t.getCursoConsult().category()))
            .collect(Collectors.toList());

            return ResponseEntity.ok().body(topicsByEmail);
        }
        return ResponseEntity.noContent().build();
    }

    // Delete
    @DeleteMapping()
    @Transactional
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<?> deleteById (@RequestBody @Valid TopicDelete topicDelete){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autorizado");
        }

        String authenticatedEmail = (String) authentication.getCredentials();
        if (!authenticatedEmail.equals(topicDelete.email())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar este topic");
        }

        Optional<TopicEntity> existTopic = topicRepository.findById(topicDelete.id());
        if(!existTopic.isPresent()){
            return ResponseEntity.notFound().build();
        }
        
        Long cursoId =  existTopic.get().getCurso().getId();
        Optional<CursosEntity> existCurso = coursesRepository.findById(cursoId);
        if(!existCurso.isPresent()){
            return ResponseEntity.notFound().build();
        }

        topicRepository.deleteById(existTopic.get().getId());
        coursesRepository.deleteById(cursoId);
        return ResponseEntity.ok("Topic eliminado correctamente");
    }

}
