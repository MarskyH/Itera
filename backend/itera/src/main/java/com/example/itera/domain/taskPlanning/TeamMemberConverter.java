package com.example.itera.domain.taskPlanning;

import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;

@Converter
public class TeamMemberConverter implements AttributeConverter<List<TeamMemberRequestDTO>, String> {

    @Override
    public String convertToDatabaseColumn(List<TeamMemberRequestDTO> members) {
        try {
            return new ObjectMapper().writeValueAsString(members);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Membros para JSON", e);
        }
    }

    @Override
    public List<TeamMemberRequestDTO> convertToEntityAttribute(String membersJson) {
        try {
            return new ObjectMapper().readValue(membersJson, new TypeReference<List<TeamMemberRequestDTO>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para Membros", e);
        }
    }
}

