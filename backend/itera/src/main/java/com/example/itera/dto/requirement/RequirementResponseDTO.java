package com.example.itera.dto.requirement;



import com.example.itera.domain.requirement.Requirement;


import java.util.List;

public record RequirementResponseDTO(String id,String title, String details, String complexity, String priority, Integer effort, Integer sizeRequirement, Integer progressiveBar, Boolean done, String iterationId, String project_id) {
    public RequirementResponseDTO(Requirement requirement){
        this(requirement.getId(), requirement.getTitle(), requirement.getDetails(), requirement.getComplexity(), requirement.getPriority(), requirement.getEffort(), requirement.getSizeRequirement(), requirement.getProgressiveBar(), requirement.getDone(), requirement.getIterationId(), requirement.getProject().getId());
    }
}

