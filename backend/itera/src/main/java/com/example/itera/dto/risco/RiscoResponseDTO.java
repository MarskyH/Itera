package com.example.itera.dto.risco;


import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;

public record RiscoResponseDTO(Long id, String titulo,  String efeito, String probabilidade, String impacto, String grauExposicao, Projeto projeto) {
    public RiscoResponseDTO(Risco risco){
        this(risco.getId(), risco.getTitulo(), risco.getEfeito(), risco.getImpacto(), risco.getGrauExposicao(), risco.getProbabilidade(), risco.getProjeto());
    }
}

