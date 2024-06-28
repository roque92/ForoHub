package org.example.forohub.dtos.cursoDTO;

import org.example.forohub.configurations.ExistingCoursesDeserializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public record CursoUpdateInfo(
    String name,
    @JsonDeserialize(using = ExistingCoursesDeserializer.class)
    CursoExistente category
) {

}
