package com.example.itera.dto.taskBug;

import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskRequirement.TaskRequirement;

public record TaskBugResponseDTO(String id, String details, String complexity, String sizeTask, String task_id, Boolean checkFront, Boolean checkBack,
                                 Boolean checkTest) {
    public TaskBugResponseDTO(TaskBug taskRequirement){
        this(taskRequirement.getId(), taskRequirement.getDetails(), taskRequirement.getComplexity(), taskRequirement.getSizeTask(), taskRequirement.getTask().getId(), taskRequirement.getCheckFront(), taskRequirement.getCheckBack(), taskRequirement.getCheckTest());
    }
}

