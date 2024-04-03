package com.example.itera.dto.riskActionType;



import com.example.itera.domain.riskActionType.RiskActionType;

public record RiskActionTypeResponseDTO(String id, String name) {
    public RiskActionTypeResponseDTO(RiskActionType riskActionType){
        this( riskActionType.getId(),  riskActionType.getName());
    }
}

