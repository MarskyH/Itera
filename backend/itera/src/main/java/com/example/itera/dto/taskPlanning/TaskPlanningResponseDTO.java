package com.example.itera.dto.taskPlanning;

import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberIdsResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.repository.user.UserRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.domain.user.User;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public record TaskPlanningResponseDTO(
        String id,
        Integer totalSize,
        Integer totalEffort,
        Double plannedSpeed,
        List<BacklogResponseDTO> projectBacklog,
        List<TeamMemberPlanningResponseDTO> projectMembers
) {
    @Autowired
    static UserRepository userRepository;
    @Autowired
    static RoleRepository roleRepository;
    @Autowired
    static ProjectRepository projectRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TaskPlanningResponseDTO(TaskPlanning taskPlanning) {
        this(
                taskPlanning.getId() != null ? taskPlanning.getId() : "",
                taskPlanning.getTotalSize(),
                taskPlanning.getTotalEffort(),
                taskPlanning.getPlannedSpeed(),
                deserializeJson(taskPlanning.getProjectBacklog(), new TypeReference<>() {}),
                deserializeJson(taskPlanning.getProjectMembers(), new TypeReference<>() {})
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
