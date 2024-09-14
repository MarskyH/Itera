package com.example.itera.dto.taskReview;

import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;

import java.util.List;

public record TaskReviewRequestDTO(
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
) {}

