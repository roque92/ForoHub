package org.example.forohub.dtos.topicDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TopicDelete(
@NotNull(message = "El id del topic es requerido")
@Positive
Long id,
@NotNull(message = "El email del usuario es requerido")
@Email
String email
) {

}
