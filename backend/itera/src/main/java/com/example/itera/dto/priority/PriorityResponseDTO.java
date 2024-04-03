package com.example.itera.dto.priority;


import com.example.itera.domain.priority.Priority;

public record PriorityResponseDTO(String id, String name) {
    public PriorityResponseDTO(Priority priority){
        this(priority.getId(),  priority.getName());
    }



}

