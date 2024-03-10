package com.example.itera.dto.projeto;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;

import java.util.List;

public record ProjetoResponseDTO(Long id, String nome, Integer prazo, Integer tempoIteracao, String nomeCliente) {
    public ProjetoResponseDTO(Projeto projeto){

        this(projeto.getId(),projeto.getNome(), projeto.getPrazo(), projeto.getTempoIteracao(), projeto.getNomeCliente());
    }
}

