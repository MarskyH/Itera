package com.example.itera.dto.task;


import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementRequestDTO;

import java.sql.Timestamp;
import java.util.List;

public record TaskTaskRequirementRequestDTO(TaskRequestDTO task, TaskRequirementRequestDTO taskRequirement, List<AssigneeRequestDTO> assignee) {
}

