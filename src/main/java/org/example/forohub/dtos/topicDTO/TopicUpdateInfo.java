package org.example.forohub.dtos.topicDTO;

import org.example.forohub.dtos.cursoDTO.CursoUpdateInfo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;



public record TopicUpdateInfo(
    String title,
    String message,
    @Valid
    @NotBlank
    String email,
    boolean status,
    CursoUpdateInfo cursoUpdateInfo

) {

}
