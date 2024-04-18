package com.example.itera.dto.taskType;



import com.example.itera.domain.taskType.TaskType;

public record TaskTypeResponseDTO(String id, String name) {
    public TaskTypeResponseDTO(TaskType taskType){
        this(taskType.getId(),  taskType.getName());
    }



}

