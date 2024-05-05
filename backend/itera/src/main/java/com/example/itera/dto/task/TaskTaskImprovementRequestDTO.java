package com.example.itera.dto.task;


import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementRequestDTO;

import java.util.List;


public record TaskTaskImprovementRequestDTO(TaskRequestDTO task, TaskImprovementRequestDTO taskImprovement, List<AssigneeRequestDTO> assignees) {
}

