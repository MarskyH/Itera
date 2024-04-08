package com.example.itera.dto.project;

import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto. risk.RiskResponseDTO;


import java.util.List;

public record ProjectWithJoinResponseDTO(ProjectResponseDTO projectResponseDTO,
                                         List<RoleResponseDTO> listaRole, List<TeamMember> listaUsersTeam, List<RiskResponseDTO> listaRisks,
                                         List<RequirementResponseDTO> listaRequirements,
                                         List<NonFunctionalRequirementProjectResponseDTO> listaRequirementsNaoFuncionais) {
    public ProjectWithJoinResponseDTO(ProjectResponseDTO projectResponseDTO, List<RoleResponseDTO> listaRole, List<TeamMember> listaUsersTeam, List<RiskResponseDTO> listaRisks, List<RequirementResponseDTO> listaRequirements, List<NonFunctionalRequirementProjectResponseDTO> listaRequirementsNaoFuncionais){

        this.projectResponseDTO = projectResponseDTO;
        this.listaRole = listaRole;
        this.listaUsersTeam = listaUsersTeam;
        this.listaRisks = listaRisks;
        this.listaRequirements = listaRequirements;
        this.listaRequirementsNaoFuncionais = listaRequirementsNaoFuncionais;
    }
}