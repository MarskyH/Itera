package com.example.itera.domain.taskPlanning;

import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;
import java.util.List;

public class TeamMemberConverter implements AttributeConverter<List<TeamMemberResponseDTO>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<TeamMemberResponseDTO> members) {
        try {
            return objectMapper.writeValueAsString(members);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Membros para JSON", e);
        }
    }

    @Override
    public List<TeamMemberResponseDTO> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<TeamMemberResponseDTO>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter JSON para Membros", e);
        }
    }
}
