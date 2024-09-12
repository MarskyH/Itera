package com.example.itera.dto.teamMember;

import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;

public record TeamMemberPlanningResponseDTO(
        String id,
        Double hourlyRate,
        String dedicatedHours,
        UserResponseDTO user,
        RoleResponseDTO role,
        ProjectResponseDTO project
) {
    public TeamMemberPlanningResponseDTO(TeamMember teamMember) {
        this(
                teamMember.getId(),
                teamMember.getHourlyRate(),
                teamMember.getDedicatedHours(),
                new UserResponseDTO(teamMember.getUser()), // Converte User para UserResponseDTO
                new RoleResponseDTO(teamMember.getRole()), // Converte Role para RoleResponseDTO
                new ProjectResponseDTO(teamMember.getProject()) // Converte Project para ProjectResponseDTO
        );
    }
}
