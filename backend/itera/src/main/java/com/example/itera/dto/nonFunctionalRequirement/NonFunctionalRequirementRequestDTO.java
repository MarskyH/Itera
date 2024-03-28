package com.example.itera.dto.nonFunctionalRequirement;

import com.example.itera.domain.project.Project;


public record NonFunctionalRequirementRequestDTO(String title, String description, String weights, Boolean multiple) {
}


