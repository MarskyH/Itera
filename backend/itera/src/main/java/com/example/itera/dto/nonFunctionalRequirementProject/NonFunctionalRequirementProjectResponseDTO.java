package com.example.itera.dto.nonFunctionalRequirementProject;



import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.nonFunctionalRequirementProject.NonFunctionalRequirementProject;
import com.example.itera.domain.requirement.Requirement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public record NonFunctionalRequirementProjectResponseDTO(String id, String project_id, String nonfunctionalrequirement_id, Integer weight) {
    public NonFunctionalRequirementProjectResponseDTO(NonFunctionalRequirementProject nonFunctionalRequirementProject){
        this(nonFunctionalRequirementProject.getId(), nonFunctionalRequirementProject.getProject().getId(), nonFunctionalRequirementProject.getNonFunctionalRequirement().getId(), nonFunctionalRequirementProject.getWeight());
    }
}


