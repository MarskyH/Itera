package com.example.itera.dto.taskBug;

import com.example.itera.domain.task.Task;
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

    public TaskBug toEntity() {
        TaskBug taskBug = new TaskBug();
        taskBug.setId(this.id);
        taskBug.setDetails(this.details);
        taskBug.setComplexity(this.complexity);
        taskBug.setSizeTask(this.sizeTask);
        Task task = new Task();
        task.setId(this.task_id);
        taskBug.setTask(task);
        taskBug.setCheckFront(this.checkFront);
        taskBug.setCheckBack(this.checkBack);
        taskBug.setCheckTest(this.checkTest);
        return taskBug;
    }
}
