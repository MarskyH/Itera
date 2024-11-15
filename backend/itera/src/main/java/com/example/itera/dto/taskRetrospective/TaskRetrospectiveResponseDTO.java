package com.example.itera.dto.taskRetrospective;

import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskRetrospective.TaskRetrospective;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


public record TaskRetrospectiveResponseDTO(
        String id,
        String strengths,
        String weaknesses,
        String improvements,
        List<TeamMemberPlanningResponseDTO> participants
) {
    @Autowired
    static UserRepository userRepository;
    @Autowired
    static RoleRepository roleRepository;
    @Autowired
    static ProjectRepository projectRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TaskRetrospectiveResponseDTO(TaskRetrospective taskRetrospective) {
        this(
                taskRetrospective.getId() != null ? taskRetrospective.getId() : "",
                taskRetrospective.getStrengths(),
                taskRetrospective.getWeaknesses(),
                taskRetrospective.getImprovements(),
                deserializeJson(taskRetrospective.getParticipants(), new TypeReference<>() {})
        );
    }

    // Função para desserializar JSON
    private static <T> List<T> deserializeJson(String json, TypeReference<List<T>> typeReference) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }
}
