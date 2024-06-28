package org.example.forohub.dtos.cursoDTO;

import org.example.forohub.configurations.ExistingCoursesDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoRegistration(
    @NotBlank(message = "El nombre del curso es requerido")
    String name,
    @NotNull(message = "La categoría del curso es requerida")
    @JsonDeserialize(using = ExistingCoursesDeserializer.class)
    @Valid
    CursoExistente category
) {

}
