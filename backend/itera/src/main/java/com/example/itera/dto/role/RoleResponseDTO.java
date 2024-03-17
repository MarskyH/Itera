package com.example.itera.dto.role;


import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.user.User;

import java.util.List;

public record RoleResponseDTO(String id, String function, String skill, String competency) {
    public RoleResponseDTO(Role role){

        this(role.getId(), role.getFunction(), role.getSkill(), role.getCompetency());
    }
}

