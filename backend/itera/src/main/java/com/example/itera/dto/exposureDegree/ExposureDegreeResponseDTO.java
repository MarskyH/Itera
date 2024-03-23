package com.example.itera.dto.exposureDegree;



import com.example.itera.domain.exposureDegree.ExposureDegree;
import com.example.itera.domain.riskActionType.RiskActionType;

public record ExposureDegreeResponseDTO(String id, String name) {
    public ExposureDegreeResponseDTO(ExposureDegree exposureDegree){
        this(exposureDegree.getId(),  exposureDegree.getName());
    }



}

