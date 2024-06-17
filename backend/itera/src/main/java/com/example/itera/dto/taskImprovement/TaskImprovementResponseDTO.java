package com.example.itera.dto.taskImprovement;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskImprovement.TaskImprovement;

public record TaskImprovementResponseDTO(String id, String details, String complexity, String sizeTask, String task_id,
                                         Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack,
                                         Boolean checkTest) {
    public TaskImprovementResponseDTO(TaskImprovement taskImprovement) {
        this(
                taskImprovement != null ? taskImprovement.getId() : "",
                taskImprovement != null ? taskImprovement.getDetails() : "",
                taskImprovement != null ? taskImprovement.getComplexity() : "",
                taskImprovement != null ? taskImprovement.getSizeTask() : "",
                taskImprovement != null && taskImprovement.getTask() != null ? taskImprovement.getTask().getId() : "",
                taskImprovement != null && taskImprovement.getCheckProject() != null ? taskImprovement.getCheckProject() : false,
                taskImprovement != null && taskImprovement.getCheckRequirement() != null ? taskImprovement.getCheckRequirement() : false,
                taskImprovement != null && taskImprovement.getCheckFront() != null ? taskImprovement.getCheckFront() : false,
                taskImprovement != null && taskImprovement.getCheckBack() != null ? taskImprovement.getCheckBack() : false,
                taskImprovement != null && taskImprovement.getCheckTest() != null ? taskImprovement.getCheckTest() : false
        );
    }

    public TaskImprovement toEntity() {
        TaskImprovement taskImprovement = new TaskImprovement();
        taskImprovement.setId(this.id);
        taskImprovement.setDetails(this.details);
        taskImprovement.setComplexity(this.complexity);
        taskImprovement.setSizeTask(this.sizeTask);
        Task task = new Task();
        task.setId(this.task_id);
        taskImprovement.setTask(task);
        taskImprovement.setCheckProject(this.checkProject);
        taskImprovement.setCheckRequirement(this.checkRequirement);
        taskImprovement.setCheckFront(this.checkFront);
        taskImprovement.setCheckBack(this.checkBack);
        taskImprovement.setCheckTest(this.checkTest);
        return taskImprovement;
    }
}
