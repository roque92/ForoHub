package org.example.forohub.dtos.topicDTO;

import java.time.LocalDateTime;

import org.example.forohub.dtos.cursoDTO.CourseRegistration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TopicRegistration(
    @NotBlank(message="El título del tópico es requerido")
    String title,
    @NotBlank(message="El mensaje del tópico es requerido")
    String message,
    LocalDateTime date,
    Boolean status,
    @NotBlank(message="El email del usuario es requerido")
    @Email
    String email,
    @NotBlank(message="El nombre del curso es requerido")
    CourseRegistration curso
)
{

}
