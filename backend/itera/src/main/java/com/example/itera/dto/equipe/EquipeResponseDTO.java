package com.example.itera.dto.equipe;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;

import java.util.List;

public record EquipeResponseDTO(Long id) {
    public EquipeResponseDTO(Equipe equipe){
        this(equipe.getId());
    }
}

