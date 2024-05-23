package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.dto.taskBug.TaskBugResponseDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementResponseDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementResponseDTO;

import java.sql.Timestamp;

public record TaskListResponseDTO(TaskResponseDTO taskData, TaskRequirementResponseDTO taskRequirementData, TaskImprovementResponseDTO taskImprovementData, TaskBugResponseDTO taskBugData) {
    public TaskListResponseDTO(Task task, TaskRequirement taskRequirement, TaskImprovement taskImprovement, TaskBug taskBug) {
        this(
                new TaskResponseDTO(task),
                new TaskRequirementResponseDTO(taskRequirement),
                new TaskImprovementResponseDTO(taskImprovement),
                new TaskBugResponseDTO(taskBug)
        );
    }


}

