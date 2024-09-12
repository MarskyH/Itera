package com.example.itera.dto.teamMember;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public record TeamMemberResponseDTO(String id, Double hourlyRate, String dedicatedHours, User user, Role role, Project project) {
    public TeamMemberResponseDTO(TeamMember teamMember){

        this(teamMember.getId(), teamMember.getHourlyRate(), teamMember.getDedicatedHours(), teamMember.getUser(), teamMember.getRole(), teamMember.getProject());
    }
}
