package com.example.itera.dto.task;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.dto.taskPlanning.TaskPlanningResponseDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementResponseDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementResponseDTO;
import com.example.itera.dto.taskBug.TaskBugResponseDTO;

import java.sql.Timestamp;
import java.util.List;

public record TaskCompleteResponseDTO(
        String id,
        String title,
        String priority,
        String details,
        String complexity,
        String effort,
        String sizeTask,
        Timestamp startDate,
        Timestamp endDate,

        Integer progressiveBar,
        Integer orderTask,
        Boolean checkCancelled,
        String detailsCancelled,
        String listName,
        String taskType,
        TaskRequirementResponseDTO taskRequirement,
        TaskImprovementResponseDTO taskImprovement,
        TaskBugResponseDTO taskBug,

        TaskPlanningResponseDTO taskPlanning,
        String iteration_id,
        List<AssigneeResponseDTO> assignees,

        List<PendencyResponseDTO> pendencies
) {
    public TaskCompleteResponseDTO(Task task, List<AssigneeResponseDTO> assignees, List<PendencyResponseDTO> pendencies) {
        this(
                task.getId() != null ? task.getId() : "",
                task.getTitle(),
                task.getPriority(),
                task.getDetails(),
                task.getComplexity(),
                task.getEffort(),
                task.getSizeRequirement(task.getSizeTask()),
                task.getStartDate(),
                task.getEndDate(),
                task.getProgressiveBar(),
                task.getOrderTask(),
                task.getCheckCancelled(),
                task.getDetailsCancelled(),
                task.getListName(),
                task.getTaskType(),
                task.getTaskRequirement() != null ? new TaskRequirementResponseDTO(task.getTaskRequirement()) : null,
                task.getTaskImprovement() != null ? new TaskImprovementResponseDTO(task.getTaskImprovement()) : null,
                task.getTaskBug() != null ? new TaskBugResponseDTO(task.getTaskBug()) : null,
                task.getTaskPlanning() != null ? new TaskPlanningResponseDTO(task.getTaskPlanning()) : null,
                task.getIteration() != null && task.getIteration().getId() != null ? task.getIteration().getId() : "",
                assignees,
                pendencies
        );
    }
}
