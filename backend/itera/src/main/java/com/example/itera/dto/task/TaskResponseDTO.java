package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import java.sql.Timestamp;

public record TaskResponseDTO(String id, String title, String priority,  String details, String complexity, String effort, String sizeTask,Timestamp startDate, Timestamp endDate, Integer progressiveBar, Integer orderTask, String listName, String taskType, String taskrequirement_id, String taskimprovement_id, String taskbug_id, String taskPlanning_id, String iteration_id) {
    public TaskResponseDTO(Task task) {
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
                task.getProgressiveBar(),
                task.getOrderTask(),
                task.getListName(),
                task.getTaskType(),
                task.getTaskRequirement() != null && task.getTaskRequirement().getId() != null ? task.getTaskRequirement().getId() : "",
                task.getTaskImprovement() != null && task.getTaskImprovement().getId() != null ? task.getTaskImprovement().getId() : "",
                task.getTaskBug() != null && task.getTaskBug().getId() != null ? task.getTaskBug().getId() : "",
                task.getTaskPlanning() != null && task.getTaskPlanning().getId() != null ? task.getTaskPlanning().getId() : "",
                task.getIteration() != null && task.getIteration().getId() != null ? task.getIteration().getId() : ""
        );
    }
}

