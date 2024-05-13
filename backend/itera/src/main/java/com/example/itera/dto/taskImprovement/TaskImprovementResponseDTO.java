package com.example.itera.dto.taskImprovement;

import com.example.itera.domain.taskImprovement.TaskImprovement;


public record TaskImprovementResponseDTO(String id, String details, String complexity, String sizeTask, String task_id,  
                                         Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack, 
                                         Boolean checkTest) {
    public TaskImprovementResponseDTO(TaskImprovement taskImprovement){
        this(taskImprovement.getId(), taskImprovement.getDetails(), taskImprovement.getComplexity(), taskImprovement.getSizeTask(), taskImprovement.getTask().getId(), taskImprovement.getCheckProject(), taskImprovement.getCheckRequirement(), taskImprovement.getCheckFront(), taskImprovement.getCheckBack(), taskImprovement.getCheckTest());
    }
}

