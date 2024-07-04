package org.example.forohub.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.example.forohub.dtos.cursoDTO.CursoDelete;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    private static final Logger log = LoggerFactory.getLogger(TopicsController.class);

    private UsersRepository userRepository;
    private CoursesRepository coursesRepository;
    private TopicRepository topicRepository;

    public TopicsController(UsersRepository userRepository, CoursesRepository coursesRepository,
            TopicRepository topicRepository, HttpServletRequest request) {
        this.userRepository = userRepository;
        this.coursesRepository = coursesRepository;
        this.topicRepository = topicRepository;
    }

    // findAll
    @GetMapping()
    public ResponseEntity<Page<TopicConsult>> listAllTopics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("topicCreationDate").descending());
        Page<TopicEntity> topics = topicRepository.findAll(pageable);
        if (topics.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<TopicConsult> allTopics = topics.stream()
                .map(t -> {
                    TopicConsult topic = new TopicConsult(
                            t.getTitleTopic(),
                            t.getBodyTopic(),
                            t.getTopicStatus(),
                            t.getTopicCreationDate(),
                            t.getUserConsult().name(),
                            t.getCursoConsult().category());
                    return topic;
                })
                .collect(Collectors.toList());

        Page<TopicConsult> resultPage = new PageImpl<>(allTopics, pageable, topics.getTotalElements());
        return ResponseEntity.ok(resultPage);
    }

    // findByID
    @GetMapping("/{id}")
    public ResponseEntity<TopicConsult> getTopicById(@PathVariable("id") Long id) {
        Optional<TopicEntity> topic = topicRepository.findById(id);
        if (topic.isPresent()) {
            TopicEntity topicEntity = topic.get();
            TopicConsult topicConsult = new TopicConsult(
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
    public ResponseEntity<?> createTopic(@RequestBody @Valid TopicRegistration topic) {
        Optional<UsersEntity> user = userRepository.findByEmail(topic.email().trim());
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
                        .body("El título del tema ya existe, busca el id del tema: " + check.get(0).getId());
            }
            check = topicRepository.findByBodyTopic(newTopic.getBodyTopic());
            if (!check.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("El mensaje del tema ya existe, busca el id: " + check.get(0).getId());
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
    public ResponseEntity<?> updateTopic(@PathVariable("id") Long id,
            @RequestBody @Valid TopicUpdateInfo topicUpdateInfo) {
        var user = userRepository.findByEmail(topicUpdateInfo.email().trim());
        if (user != null) {
            Optional<TopicEntity> existTopic = topicRepository.findByIdAndAuthorId(id, user.get().getId());
            if (existTopic.isPresent()) {
                TopicEntity updatedInfo = existTopic.get();
                if (topicUpdateInfo.title() != null) {
                    if (updatedInfo.getTitleTopic() != topicUpdateInfo.title()) {
                        updatedInfo.setTitleTopic(topicUpdateInfo.title());
                    }
                    throw new IllegalArgumentException("El titulo del topic ya existe.");
                }
                if (topicUpdateInfo.message() != null) {
                    if (updatedInfo.getBodyTopic() != topicUpdateInfo.message()) {
                        updatedInfo.setBodyTopic(topicUpdateInfo.message());
                    }
                    throw new IllegalArgumentException("El mensaje del topic ya existe.");
                }
                if (updatedInfo.getTopicStatus() != topicUpdateInfo.status()) {
                    updatedInfo.setTopicStatus(topicUpdateInfo.status());
                }
                if (topicUpdateInfo.cursoUpdateInfo() != null) {
                    updatedInfo.actualizarCurso(topicUpdateInfo.cursoUpdateInfo());
                }

                topicRepository.save(updatedInfo);
                return ResponseEntity.ok().body("Datos actualizados correctamente");

            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("Usuario no encontrado");
    }

    @GetMapping("userTopic/{email}")
    public ResponseEntity<List<TopicConsult>> topicsByEmail(@PathVariable("email") String email) {
        Optional<UsersEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            List<TopicEntity> topics = topicRepository.findAllByAuthorId(user.get().getId());
            if (topics.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<TopicConsult> topicsByEmail = topics.stream()
            .map(t -> {
                TopicConsult topic = new TopicConsult(
                t.getTitleTopic(),
                t.getBodyTopic(),
                t.getTopicStatus(),
                t.getTopicCreationDate(),
                t.getUserConsult().name(),
                t.getCursoConsult().category());
                return topic;    
            })
            .collect(Collectors.toList());

            return ResponseEntity.ok().body(topicsByEmail);
        }
        return ResponseEntity.noContent().build();
    }

    // Delete
    @DeleteMapping()
    @Transactional
    public ResponseEntity<?> deleteById (@RequestBody @Valid TopicDelete topicDelete){
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
