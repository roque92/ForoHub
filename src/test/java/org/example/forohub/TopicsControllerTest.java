package org.example.forohub;

import org.example.forohub.controllers.TopicsController;
import org.example.forohub.dtos.cursoDTO.CourseRegistration;
import
 org.example.forohub.dtos.cursoDTO.ExistingCourses;
import org.example.forohub.dtos.topicDTO.TopicRegistration;
import org.example.forohub.entities.CursosEntity;
import org.example.forohub.entities.TopicEntity;
import org.example.forohub.entities.UsersEntity;
import org.example.forohub.repositories.CoursesRepository;
import org.example.forohub.repositories.TopicRepository;
import org.example.forohub.repositories.UsersRepository;
import org.
junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TopicsControllerTest {

    @Mock
    private TopicRepository topicRepository;
    @Mock
    private UsersRepository userRepository;
    @Mock
    private CoursesRepository coursesRepository;

    @InjectMocks
    private TopicsController topicsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNewTopic() {
        // Datos de prueba
        TopicRegistration topicRegistration = new TopicRegistration(
                "Título del tópico",
                "Mensaje del tópico",
                LocalDateTime.now(),
                true,
                "test@example.com",
                new CourseRegistration("Nombre del curso", ExistingCourses.JAVA)
        );

        UsersEntity user = new UsersEntity();
        user.setId(1L);
        CursosEntity nuevoCurso = new CursosEntity(topicRegistration.curso());
        nuevoCurso.setId(1L); // Asignamos un ID al nuevo curso

        // Simulamos el comportamiento de los repositorios
        when(userRepository.findByEmail(topicRegistration.email())).thenReturn(Optional.of(user).get()); 
// O, para manejar el caso en que el usuario no existe:
// when(userRepository.findByEmail(topicRegistration.email())).thenReturn(Optional.of(user).orElse(null));

        when(coursesRepository.save(any(CursosEntity.class))).thenReturn(nuevoCurso);
        when(topicRepository.save(any(TopicEntity.class))).thenReturn(null); // No necesitamos el valor de retorno en esta prueba

        // Llamamos a la función que queremos probar
        ResponseEntity<?> response = topicsController.newTopic(topicRegistration);

        // Verificamos el resultado
        assertEquals(200, response.getStatusCodeValue()); // Verificamos el código de estado HTTP
        assertEquals("Topic and course created successfully", response.getBody()); // Verificamos el mensaje de respuesta

        // Verificamos que los repositorios fueron llamados correctamente
        }
}
