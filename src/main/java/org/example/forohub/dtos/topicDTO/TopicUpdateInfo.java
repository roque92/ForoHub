package org.example.forohub.dtos.topicDTO;

import org.example.forohub.dtos.cursoDTO.CursoUpdateInfo;




public record TopicUpdateInfo(
    String title,
    String message,
    boolean status,
    CursoUpdateInfo curso

) {

}
