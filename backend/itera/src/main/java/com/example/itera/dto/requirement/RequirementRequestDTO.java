package com.example.itera.dto.requirement;


import com.example.itera.domain.project.Project;


public record RequirementRequestDTO(String name, String title, String details, String complexity, String priority,
                                  Integer effort, Integer sizeRequirement, Project project) {
}


