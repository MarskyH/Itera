package com.example.itera.dto.assignee;

import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.task.TaskStep;

public record AssigneeResponseDTO(String id, TaskStep taskStep, Integer deadline, String user_id, String task_id)  {
    public AssigneeResponseDTO(Assignee assignee){
        this(assignee.getId(), assignee.getTaskStep(), assignee.getDeadline(), assignee.getUser().getId(), assignee.getTask().getId());
    }
}

