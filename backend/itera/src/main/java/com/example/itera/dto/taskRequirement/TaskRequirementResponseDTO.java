package com.example.itera.dto.taskRequirement;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskRequirement.TaskRequirement;

import java.sql.Timestamp;

public record TaskRequirementResponseDTO(String id, String details, String complexity, Integer sizeTask, String task_id,
                                         Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack,
                                         Boolean checkTest) {
    public TaskRequirementResponseDTO(TaskRequirement taskRequirement){
        this(taskRequirement.getId(), taskRequirement.getDetails(), taskRequirement.getComplexity(), taskRequirement.getSizeTask(), taskRequirement.getTask().getId(), taskRequirement.getCheckProject(), taskRequirement.getCheckRequirement(), taskRequirement.getCheckFront(), taskRequirement.getCheckBack(), taskRequirement.getCheckTest());
    }
}

