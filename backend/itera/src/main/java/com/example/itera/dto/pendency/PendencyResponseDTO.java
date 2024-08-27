package com.example.itera.dto.pendency;


import com.example.itera.domain.pendency.Pendency;
import java.sql.Timestamp;

public record PendencyResponseDTO(String id, String title, String description, Timestamp creationDate, Timestamp endDate, Boolean status, String task_id)  {
    public PendencyResponseDTO(Pendency pendency){
        this(pendency.getId(), pendency.getTitle(), pendency.getDescription(), pendency.getCreationDate(), pendency.getEndDate(), pendency.getStatus(), pendency.getTask().getId());
    }
}

