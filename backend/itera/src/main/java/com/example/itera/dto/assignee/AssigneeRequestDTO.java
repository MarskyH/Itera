package com.example.itera.dto.assignee;


import com.example.itera.domain.task.TaskStep;

public record AssigneeRequestDTO(String taskStep, Integer deadline, String user_id, String task_id) {
}

