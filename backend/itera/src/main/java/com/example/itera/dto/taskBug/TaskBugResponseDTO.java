package com.example.itera.dto.taskBug;

import com.example.itera.domain.taskBug.TaskBug;

public record TaskBugResponseDTO(
        String id,
        String details,
        String complexity,
        String sizeTask,
        String task_id,
        Boolean checkFront,
        Boolean checkBack,
        Boolean checkTest
) {
    public TaskBugResponseDTO(TaskBug taskBug){
        this(
                taskBug != null ? taskBug.getId() : null,
                taskBug != null ? taskBug.getDetails() : null,
                taskBug != null ? taskBug.getComplexity() : null,
                taskBug != null ? taskBug.getSizeTask() : null,
                taskBug != null && taskBug.getTask() != null ?  taskBug.getTask().getId() : "",
                taskBug != null && taskBug.getCheckFront() != null ? taskBug.getCheckFront() : false,
                taskBug != null && taskBug.getCheckBack() != null ? taskBug.getCheckBack() : false,
                taskBug != null && taskBug.getCheckTest() != null ? taskBug.getCheckTest() : false
        );
    }
}
