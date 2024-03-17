package com.example.itera.dto.nonFunctionalRequirement;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;

public record NonFunctionalRequirementResponseDTO(String id, String title, Integer valueRequirement, String project_id) {
    public NonFunctionalRequirementResponseDTO(NonFunctionalRequirement nonFunctionalRequirement){
        this(nonFunctionalRequirement.getId(), nonFunctionalRequirement.getTitle(), nonFunctionalRequirement.getValueRequirement(), nonFunctionalRequirement.getProject().getId());
    }
}

