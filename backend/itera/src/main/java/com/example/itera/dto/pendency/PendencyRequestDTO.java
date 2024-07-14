package com.example.itera.dto.pendency;


import com.example.itera.domain.task.TaskStep;

public record PendencyRequestDTO(String title, String description, Boolean status, String task_id) {
}

