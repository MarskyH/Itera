package com.example.itera.dto.taskRequirement;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskRequirement.TaskRequirement;

public record TaskRequirementResponseDTO(String id, String details, String complexity, Integer sizeTask, String task_id,
                                         Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack,
                                         Boolean checkTest) {
    public TaskRequirementResponseDTO(TaskRequirement taskRequirement){
        this(taskRequirement.getId(), taskRequirement.getDetails(), taskRequirement.getComplexity(), taskRequirement.getSizeTask(), taskRequirement.getTask().getId(), taskRequirement.getCheckProject(), taskRequirement.getCheckRequirement(), taskRequirement.getCheckFront(), taskRequirement.getCheckBack(), taskRequirement.getCheckTest());
    }

    public TaskRequirement toEntity() {
        TaskRequirement taskRequirement = new TaskRequirement();
        taskRequirement.setId(this.id);
        taskRequirement.setDetails(this.details);
        taskRequirement.setComplexity(this.complexity);
        taskRequirement.setSizeTask(this.sizeTask);
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
