package com.example.itera.dto.taskReview;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.taskReview.TaskReview;
import com.example.itera.domain.user.User;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberIdsResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record TaskReviewResponseDTO(
        String id,
        Integer totalSize,
        Integer totalEffort,
        Double completedSpeed,
        List<BacklogResponseDTO> iterationBacklog,
        List<BacklogResponseDTO> completedScope,
        List<TeamMemberPlanningResponseDTO> participatingMembers,
        Boolean checkHumanResources,
        Boolean checkScope,
        Boolean checkSpeed,
        Boolean checkRisks

) {
    @Autowired
    static UserRepository userRepository;
    @Autowired
    static RoleRepository roleRepository;
    @Autowired
    static ProjectRepository projectRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TaskReviewResponseDTO(TaskReview taskReview) {
        this(
                taskReview.getId() != null ? taskReview.getId() : "",
                taskReview.getTotalSize(),
                taskReview.getTotalEffort(),
                taskReview.getCompletedSpeed(),
                deserializeJson(taskReview.getIterationBacklog(), new TypeReference<>() {}),
                deserializeJson(taskReview.getCompletedScope(), new TypeReference<>() {}),
                deserializeJson(taskReview.getParticipatingMembers(), new TypeReference<>() {}),
                taskReview.getCheckHumanResources(),
                taskReview.getCheckScope(),
                taskReview.getCheckSpeed(),
                taskReview.getCheckRisks()
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
