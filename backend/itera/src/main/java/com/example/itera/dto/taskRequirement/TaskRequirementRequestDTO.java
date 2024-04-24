package com.example.itera.dto.taskRequirement;


import java.sql.Timestamp;

public record TaskRequirementRequestDTO(String details, String complexity, String sizeTask, String effort, String task_id) {
}

