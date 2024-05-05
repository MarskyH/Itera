package com.example.itera.dto.taskImprovement;

import com.example.itera.domain.taskRequirement.TaskRequirement;

public record TaskImprovementResponseDTO(String id, String details, String complexity, String sizeTask, String task_id) {
    public TaskImprovementResponseDTO(TaskRequirement taskRequirement){
        this(taskRequirement.getId(), taskRequirement.getDetails(), taskRequirement.getComplexity(), taskRequirement.getSizeTask(), taskRequirement.getTask().getId());
    }
}

