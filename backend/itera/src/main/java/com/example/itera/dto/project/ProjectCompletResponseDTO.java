package com.example.itera.dto.project;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.user.User;
import com.example.itera.dto.activity.ActivityResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto. risk.RiskResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;

import java.util.List;

public record ProjectCompletResponseDTO(String id, String name, Integer deadline, Integer iterationTime, String clientName, String createdBy,
                                        List<RoleResponseDTO> listaRole, List<UserResponseDTO> listaUsersTeam, List<RiskResponseDTO> listaRisks,

                                        List<ActivityResponseDTO> listaAcoes, List<RequirementResponseDTO> listaRequirements,
                                        List<NonFunctionalRequirementResponseDTO> listaRequirementsNaoFuncionais) {
    public ProjectCompletResponseDTO(Project project, List<RoleResponseDTO> listaRole, List<UserResponseDTO> listaUsersTeam,  List<RiskResponseDTO> listaRisks,  List<ActivityResponseDTO> listaAcoes, List<RequirementResponseDTO> listaRequirements, List<NonFunctionalRequirementResponseDTO> listaRequirementsNaoFuncionais){

        this(project.getId(),project.getName(), project.getDeadline(), project.getIterationTime(), project.getClientName(), project.getCreatedBy(), listaRole, listaUsersTeam, listaRisks, listaAcoes, listaRequirements, listaRequirementsNaoFuncionais);
    }
}



