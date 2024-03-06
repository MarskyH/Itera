package com.example.itera.dto.requisitoNaoFuncional;


import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;

public record RequisitoNaoFuncionalResponseDTO(Long id, String titulo, String detalhamento, String complexidade, String prioridade, Integer esforco, Integer tamanho, Projeto projeto) {
    public RequisitoNaoFuncionalResponseDTO(Requisito requisito){
        this(requisito.getId(), requisito.getTitulo(), requisito.getDetalhamento(), requisito.getComplexidade(), requisito.getPrioridade(), requisito.getEsforco(), requisito.getTamanho(), requisito.getProjeto());
    }
}

