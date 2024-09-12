package com.example.itera.domain.taskPlanning;

import com.example.itera.dto.project.BacklogRequestDTO;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class BacklogConverter implements AttributeConverter<List<BacklogRequestDTO>, String> {

    @Override
    public String convertToDatabaseColumn(List<BacklogRequestDTO> backlog) {
        try {
            return new ObjectMapper().writeValueAsString(backlog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Backlog para JSON", e);
        }
    }

    @Override
    public List<BacklogRequestDTO> convertToEntityAttribute(String backlogJson) {
        try {
            return new ObjectMapper().readValue(backlogJson, new TypeReference<List<BacklogRequestDTO>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para Backlog", e);
        }
    }
}

