package com.example.itera.dto.task;

import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.pendency.PendencyRequestDTO;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.dto.taskBug.TaskBugRequestDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementRequestDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementRequestDTO;

import java.sql.Timestamp;
import java.util.List;

public record TaskCompleteRequestDTO(
        String title,
        String priority,
        String details,
        String complexity,
        String effort,
        String sizeTask,
        Timestamp startDate,
        Timestamp endDate,
        Integer orderTask,
        Boolean checkCancelled,
        String detailsCancelled,
        String listName,
        String taskType,
        TaskRequirementRequestDTO taskRequirement,
        TaskImprovementRequestDTO taskImprovement,
        TaskBugRequestDTO taskBug,
        String iteration_id,
        List<AssigneeRequestDTO> assigneies,
        List<PendencyRequestDTO> pendencies
) {
}
