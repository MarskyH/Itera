package com.example.itera.dto.activity;

import com.example.itera.domain.activity.Activity;
import com.example.itera.domain. risk.Risk;

public record ActivityResponseDTO(String id, String title, String description, String type, String risk_id) {
    public ActivityResponseDTO(Activity activity){
        this(activity.getId(), activity.getTitle(), activity.getDescription(), activity.getType(), activity.getRisk().getId());
    }
}

