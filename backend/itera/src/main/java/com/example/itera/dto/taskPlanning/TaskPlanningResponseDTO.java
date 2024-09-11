package com.example.itera.dto.taskPlanning;

import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public record TaskPlanningResponseDTO(
        String id,
        Integer totalSize,
        Integer totalEffort,
        Double plannedSpeed,
        List<BacklogResponseDTO> projectBacklog,
        List<TeamMemberResponseDTO> projectMembers,
        TaskResponseDTO task
) {
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

    private static <T> List<T> deserializeJson(String json, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }
}

