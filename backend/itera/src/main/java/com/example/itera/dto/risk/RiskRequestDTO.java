package com.example.itera.dto. risk;

import com.example.itera.domain.project.Project;


public record RiskRequestDTO(String title,  String effect, String probability, String impact, String exposureDegree, Project project) {
}


