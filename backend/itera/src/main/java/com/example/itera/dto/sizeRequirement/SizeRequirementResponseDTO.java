package com.example.itera.dto.sizeRequirement;



import com.example.itera.domain.sizeRequirement.SizeRequirement;
import com.example.itera.domain.taskType.TaskType;

public record SizeRequirementResponseDTO(String id, String name, Integer function_points) {
    public SizeRequirementResponseDTO(SizeRequirement sizeRequirement){
        this(sizeRequirement.getId(),  sizeRequirement.getName(), sizeRequirement.getFunction_points());
    }



}

