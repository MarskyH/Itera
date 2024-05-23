package com.example.itera.dto.iteration;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.requirement.RequirementResponseDTO;

import java.sql.Timestamp;
import java.util.List;

public record IterationRequirementResponseDTO(String id, Integer number, Timestamp startDate, Timestamp endDate, Boolean active, String project_id, List<RequirementResponseDTO> requirements) {
    public IterationRequirementResponseDTO(IterationResponseDTO iteration, List<RequirementResponseDTO> requirements){
        this(iteration.id(), iteration.number(), iteration.startDate(), iteration.endDate(), iteration.active(), iteration.project_id(), requirements);
    }
}


