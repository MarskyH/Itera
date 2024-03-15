package com.example.itera.dto.papel;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;

import java.util.List;

public record PapelResponseDTO(Long id, String funcao, String habilidade, String competencia) {
    public PapelResponseDTO(Papel papel){

        this(papel.getId(), papel.getFuncao(), papel.getHabilidade(), papel.getCompetencia());
    }
}

