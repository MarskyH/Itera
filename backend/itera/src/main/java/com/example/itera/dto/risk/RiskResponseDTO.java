package com.example.itera.dto. risk;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;

public record RiskResponseDTO(String id, String title,  String effect, String probability, String impact, String exposureDegree, String description, String type, String project_id) {
    public RiskResponseDTO(Risk  risk){
        this( risk.getId(),  risk.getTitle(),  risk.getEffect(),  risk.getImpact(),  risk.getExposureDegree(),  risk.getProbability(), risk.getDescription(), risk.getType(), risk.getProject().getId());
    }
}

