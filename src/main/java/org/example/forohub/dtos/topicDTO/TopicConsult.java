package org.example.forohub.dtos.topicDTO;

import java.time.LocalDateTime;

import org.example.forohub.dtos.cursoDTO.CursoConsult;
import org.example.forohub.dtos.cursoDTO.CursoExistente;
import org.example.forohub.dtos.userDTO.UserConsult;

public record TopicConsult(
        Long id,
    String titleTopic,
    String bodytopic,
    boolean statusTopic,
    LocalDateTime topicCreationDate,
    UserConsult userConsult,
    CursoConsult cursoConsult
) {

    public TopicConsult(Long id, String titleTopic2, String bodyTopic2, Boolean topicStatus, LocalDateTime topicCreationDate2,
            String name, CursoExistente category) {
        this(id, titleTopic2, bodyTopic2, topicStatus, topicCreationDate2, new UserConsult(name), new CursoConsult(category));
    }

}
