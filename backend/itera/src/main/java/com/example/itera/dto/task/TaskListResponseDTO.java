package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.dto.taskBug.TaskBugResponseDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementResponseDTO;
import com.example.itera.dto.taskPlanning.TaskPlanningResponseDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementResponseDTO;

import java.sql.Timestamp;

public record TaskListResponseDTO(
        TaskResponseDTO taskData,
        TaskRequirementResponseDTO taskRequirementData,
        TaskImprovementResponseDTO taskImprovementData,
        TaskBugResponseDTO taskBugData,
        TaskPlanningResponseDTO taskPlanningResponseDTO) {

    public TaskListResponseDTO(Task task, TaskRequirement taskRequirement, TaskImprovement taskImprovement, TaskBug taskBug, TaskPlanning taskPlanning) {
        this(
                new TaskResponseDTO(task),
                taskRequirement != null ? new TaskRequirementResponseDTO(taskRequirement) : null,
                taskImprovement != null ? new TaskImprovementResponseDTO(taskImprovement) : null,
                taskBug != null ? new TaskBugResponseDTO(taskBug) : null,
                taskPlanning != null ? new TaskPlanningResponseDTO(taskPlanning) : null
        );
    }
}


