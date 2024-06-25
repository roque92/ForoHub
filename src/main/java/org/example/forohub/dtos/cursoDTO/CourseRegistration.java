package org.example.forohub.dtos.cursoDTO;

import jakarta.validation.constraints.NotBlank;

public record CourseRegistration(
    @NotBlank(message = "El nombre del curso es requerido")
    String name,
    @NotBlank(message = "La categoría del curso es requerida")
    ExistingCourses category
) {

}
