package com.example.itera.dto.project;




import com.example.itera.domain.project.Project;


public record ProjectResponseDTO(String id, String name, Integer deadline, Integer iterationTime, Integer workHours, String clientName, String createdBy) {
    public ProjectResponseDTO(Project project){

        this(project.getId(),project.getName(), project.getDeadline(), project.getIterationTime(), project.getWorkHours(), project.getClientName(), project.getCreatedBy());
    }
}


