package com.example.itera.dto.task;


import com.example.itera.dto.taskRequirement.TaskRequirementRequestDTO;

import java.sql.Timestamp;

public record TaskTaskRequirementRequestDTO(TaskRequestDTO task, TaskRequirementRequestDTO taskRequirement) {
}

