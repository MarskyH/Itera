package com.example.itera.dto.iteration;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.task.Task;
import com.example.itera.dto.task.TaskListResponseDTO;

import java.sql.Timestamp;

public record IterationResponseDTO(String id, Integer number, Timestamp startDate, Timestamp endDate, Boolean active, String project_id) {
    public IterationResponseDTO(Iteration iteration){
        this(iteration.getId(), iteration.getNumber(), iteration.getStartDate(), iteration.getEndDate(), iteration.getActive(), iteration.getProject().getId());
    }
}


