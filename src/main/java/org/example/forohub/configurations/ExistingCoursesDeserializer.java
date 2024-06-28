package org.example.forohub.configurations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.example.forohub.dtos.cursoDTO.CursoExistente;

import java.io.IOException;

public class ExistingCoursesDeserializer extends JsonDeserializer<CursoExistente> {

    @Override
    public CursoExistente deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String courseName = p.getText();
        if (courseName == null || courseName.isBlank()) {
            throw new JsonMappingException(p, "Invalid course category: " + courseName);
        }
        for (CursoExistente course : CursoExistente.values()) {
            if (course.matches(courseName)) {
                return course; 
            }
        }
        return CursoExistente.OTHER;
    }
}
