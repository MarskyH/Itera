package com.example.itera.dto.requisito;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;

import java.util.List;

public record RequisitoResponseDTO(Long id,String titulo, String detalhamento, String complexidade, String prioridade, Integer esforco, Integer tamanho, Long projeto_id) {
    public RequisitoResponseDTO(Requisito requisito){
        this(requisito.getId(), requisito.getTitulo(), requisito.getDetalhamento(), requisito.getComplexidade(), requisito.getPrioridade(), requisito.getEsforco(), requisito.getTamanho(), requisito.getProjeto().getId());
    }
}

