package com.example.itera.dto.requirement;


import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;

import java.util.List;

public record RequirementResponseDTO(String id,String title, String details, String complexity, String priority, Integer effort, Integer sizeRequirement, String project_id) {
    public RequirementResponseDTO(Requirement requirement){
        this(requirement.getId(), requirement.getTitle(), requirement.getDetails(), requirement.getComplexity(), requirement.getPriority(), requirement.getEffort(), requirement.getSizeRequirement(), requirement.getProject().getId());
    }
}

