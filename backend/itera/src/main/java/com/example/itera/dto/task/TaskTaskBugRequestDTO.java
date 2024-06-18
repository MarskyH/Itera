package com.example.itera.dto.task;


import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.taskBug.TaskBugRequestDTO;

import java.util.List;


public record TaskTaskBugRequestDTO(TaskRequestBugDTO task, TaskBugRequestDTO taskBug, List<AssigneeRequestDTO> assignees) {
}

