package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import java.sql.Timestamp;

public record TaskResponseDTO(String id, String title, String priority, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, String taskrequirement_id, String taskimprovement_id, String taskbug_id, String iteration_id) {
    public TaskResponseDTO(Task task){
        this(task.getId(), task.getTitle(), task.getPriority(), task.getStartDate(), task.getEndDate(), task.getOrderTask(), task.getListName(), task.getTaskType(), task.getTaskRequirement().getId(),  task.getTaskImprovement().getId(), task.getTaskBug().getId(), task.getIteration().getId());
    }
}

