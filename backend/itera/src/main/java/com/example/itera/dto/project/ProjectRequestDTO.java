package com.example.itera.dto.project;

import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;

import java.util.List;

public record ProjectRequestDTO(String name, Integer deadline, Integer iterationTime, Integer workHours, String clientName) {
}


