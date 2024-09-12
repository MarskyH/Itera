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
        List<TeamMemberPlanningResponseDTO> projectMembers,
        TaskResponseDTO task
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
                deserializeJson(taskPlanning.getProjectMembers(), new TypeReference<>() {}),
                taskPlanning.getTask() != null ? new TaskResponseDTO(taskPlanning.getTask()) : null
        );
    }

    // Função para desserializar e completar os membros da equipe
    private static List<TeamMemberPlanningResponseDTO> completeTeamMembers(String jsonTeamMembers) {
        // Desserializa o JSON para uma lista de TeamMemberIdsDTO (com apenas os IDs)
        List<TeamMemberIdsResponseDTO> teamMembersWithIds = deserializeJson(jsonTeamMembers, new TypeReference<>() {});

        List<TeamMemberPlanningResponseDTO> completeTeamMembers = new ArrayList<>();
        for (TeamMemberIdsResponseDTO memberWithIds : teamMembersWithIds) {
            System.out.println("TESTE: " + memberWithIds);
            System.out.println("TESTE: " + memberWithIds.dedicatedHours());
            System.out.println("TESTE: " + memberWithIds.hourlyRate());
            System.out.println("TESTE: " + memberWithIds.id());
            System.out.println("TESTE: " + memberWithIds.user_id());
            System.out.println("TESTE: " + memberWithIds.role_id());
            System.out.println("TESTE: " + memberWithIds.project_id());
            // Busca os objetos completos pelos IDs
            User user = userRepository.findById(memberWithIds.user_id()).orElseThrow();
            Role role = roleRepository.findById(memberWithIds.role_id()).orElseThrow();
            Project project = projectRepository.findById(memberWithIds.project_id()).orElseThrow();

            // Cria o DTO com os objetos completos
            TeamMemberPlanningResponseDTO completeMember = new TeamMemberPlanningResponseDTO(
                    memberWithIds.id(),
                    memberWithIds.hourlyRate(),
                    memberWithIds.dedicatedHours(),
                    new UserResponseDTO(user),  // Objeto User completo
                    new RoleResponseDTO(role),  // Objeto Role completo
                    new ProjectResponseDTO(project)  // Objeto Project completo
            );

            completeTeamMembers.add(completeMember);
        }

        return completeTeamMembers;
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
