package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.taskRequirement.TaskRequirementResponseDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementResponseDTO;
import com.example.itera.dto.taskBug.TaskBugResponseDTO;

import java.sql.Timestamp;

public record TaskGetResponseDTO(
        String id,
        String title,
        String priority,
        String details,
        String complexity,
        String effort,
        Integer sizeTask,
        Timestamp startDate,
        Timestamp endDate,
        Integer orderTask,
        String listName,
        String taskType,
        TaskRequirementResponseDTO taskRequirement,
        TaskImprovementResponseDTO taskImprovement,
        TaskBugResponseDTO taskBug,
        String iteration_id
) {
    public TaskGetResponseDTO(Task task) {
        this(
                task.getId() != null ? task.getId() : "",
                task.getTitle(),
                task.getPriority(),
                task.getDetails(),
                task.getComplexity(),
                task.getEffort(),
                task.getSizeTask(),
                task.getStartDate(),
                task.getEndDate(),
                task.getOrderTask(),
                task.getListName(),
                task.getTaskType(),
                task.getTaskRequirement() != null ? new TaskRequirementResponseDTO(task.getTaskRequirement()) : null,
                task.getTaskImprovement() != null ? new TaskImprovementResponseDTO(task.getTaskImprovement()) : null,
                task.getTaskBug() != null ? new TaskBugResponseDTO(task.getTaskBug()) : null,
                task.getIteration() != null && task.getIteration().getId() != null ? task.getIteration().getId() : ""
        );
    }
}
