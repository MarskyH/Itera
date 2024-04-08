package com.example.itera.dto.project;




import com.example.itera.domain.project.Project;

import java.sql.Timestamp;


public record ProjectResponseDTO(String id, String name, Integer deadline, Integer iterationTime, Integer workHours, String clientName, String createdBy, Timestamp creationDate, Timestamp modificationDate, Timestamp lastAccessDate) {
    public ProjectResponseDTO(Project project){

        this(project.getId(),project.getName(), project.getDeadline(), project.getIterationTime(), project.getWorkHours(), project.getClientName(), project.getCreatedBy(), project.getCreationDate(), project.getModificationDate(), project.getLastAccessDate());
    }
}


