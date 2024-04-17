package com.example.itera.dto.project;

import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto. risk.RiskResponseDTO;


import java.util.List;

public record ProjectWithJoinResponseDTO(ProjectResponseDTO project,
                                         List<RoleResponseDTO> roles, List<TeamMember> members, List<RiskResponseDTO> risks,
                                         List<RequirementResponseDTO> functionalRequirements,
                                         List<NonFunctionalRequirementProjectResponseDTO> nonFunctionalRequirements) {
    public ProjectWithJoinResponseDTO(ProjectResponseDTO project, List<RoleResponseDTO> roles, List<TeamMember> members, List<RiskResponseDTO> risks, List<RequirementResponseDTO> functionalRequirements, List<NonFunctionalRequirementProjectResponseDTO> nonFunctionalRequirements){

        this.project = project;
        this.roles = roles;
        this.members = members;
        this.risks = risks;
        this.functionalRequirements = functionalRequirements;
        this.nonFunctionalRequirements = nonFunctionalRequirements;
    }
}