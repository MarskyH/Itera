package com.example.itera.dto.project;


import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;

import java.util.List;

public record ProjectResponseDTO(String id, String name, Integer deadline, Integer iterationTime, Integer workHours, String clientName, String createdBy) {
    public ProjectResponseDTO(Project project){

        this(project.getId(),project.getName(), project.getDeadline(), project.getIterationTime(), project.getWorkHours(), project.getClientName(), project.getCreatedBy());
    }
}


