package com.example.itera.dto.taskRequirement;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;

public record TaskRequirementResponseDTO(String id,  String task_id,
                                         Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack,
                                         Boolean checkTest) {
    public TaskRequirementResponseDTO(TaskRequirement taskRequirement){
        this(
                taskRequirement != null ? taskRequirement.getId() : "",
                taskRequirement != null && taskRequirement.getTask() != null ? taskRequirement.getTask().getId() : "",
                taskRequirement != null && taskRequirement.getCheckProject() != null ? taskRequirement.getCheckProject() : false,
                taskRequirement != null && taskRequirement.getCheckRequirement() != null ? taskRequirement.getCheckRequirement() : false,
                taskRequirement != null && taskRequirement.getCheckFront() != null ? taskRequirement.getCheckFront() : false,
                taskRequirement != null && taskRequirement.getCheckBack() != null ? taskRequirement.getCheckBack() : false,
                taskRequirement != null && taskRequirement.getCheckTest() != null ? taskRequirement.getCheckTest() : false
        );
    }



    public TaskRequirement toEntity() {
        TaskRequirement taskRequirement = new TaskRequirement();
        taskRequirement.setId(this.id);
        Task task = new Task();
        task.setId(this.task_id);
        taskRequirement.setTask(task);
        taskRequirement.setCheckProject(this.checkProject);
        taskRequirement.setCheckRequirement(this.checkRequirement);
        taskRequirement.setCheckFront(this.checkFront);
        taskRequirement.setCheckBack(this.checkBack);
        taskRequirement.setCheckTest(this.checkTest);
        return taskRequirement;
    }
}
