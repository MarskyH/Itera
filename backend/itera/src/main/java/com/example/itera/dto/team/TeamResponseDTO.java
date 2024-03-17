package com.example.itera.dto.team;


import com.example.itera.domain.team.Team;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.user.User;

import java.util.List;

public record TeamResponseDTO(String id) {
    public TeamResponseDTO(Team team){
        this(team.getId());
    }
}

