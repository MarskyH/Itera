package com.example.itera.domain.taskPlanning;

import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class BacklogConverter implements AttributeConverter<List<BacklogResponseDTO>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<BacklogResponseDTO> backlog) {
        try {
            return objectMapper.writeValueAsString(backlog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Backlog para JSON", e);
        }
    }

    @Override
    public List<BacklogResponseDTO> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<BacklogResponseDTO>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para Backlog", e);
        }
    }
}

