package com.example.itera.dto.taskRequirement;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskRequirement.TaskRequirement;

import java.sql.Timestamp;

public record TaskRequirementResponseDTO(String id, String details, String complexity, String sizeTask, String task_id) {
    public TaskRequirementResponseDTO(TaskRequirement taskRequirement){
        this(taskRequirement.getId(), taskRequirement.getDetails(), taskRequirement.getComplexity(), taskRequirement.getSizeTask(), taskRequirement.getTask().getId());
    }
}

