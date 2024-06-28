package org.example.forohub.dtos.topicDTO;

import org.example.forohub.dtos.cursoDTO.CursoRegistration;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicRegistration(
    @NotBlank(message="El título del tópico es requerido")
    String title,
    @NotBlank(message="El mensaje del tópico es requerido")
    String message,
    @NotBlank(message="El email del usuario es requerido")
    @Email
    String email,
    @NotNull(message="El nombre del curso es requerido")
    @Valid
    CursoRegistration curso
)
{

}
