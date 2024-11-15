package com.example.itera.dto.taskRetrospective;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;

import java.util.List;

public record TaskRetrospectiveRequestDTO(
         String strengths,
         String weaknesses,
         String improvements,
         List<TeamMemberPlanningResponseDTO> participants
) {}
