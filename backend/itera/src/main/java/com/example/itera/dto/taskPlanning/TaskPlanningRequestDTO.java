package com.example.itera.dto.taskPlanning;

import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;

import java.util.List;

public record TaskPlanningRequestDTO(
        Integer totalSize,
        Integer totalEffort,
        Double plannedSpeed,
        List<BacklogResponseDTO> projectBacklog,
        List<TeamMemberResponseDTO> projectMembers,
        String taskId
) {}
