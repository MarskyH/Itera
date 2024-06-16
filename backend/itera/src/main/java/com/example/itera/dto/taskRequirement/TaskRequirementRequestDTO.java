package com.example.itera.dto.taskRequirement;


import java.sql.Timestamp;

public record TaskRequirementRequestDTO(String details, String complexity, Integer sizeTask, Integer effort, String task_id) {
}

