package com.example.itera.dto.requisitoNaoFuncional;


import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;

public record RequisitoNaoFuncionalResponseDTO(Long id, String titulo, Integer valor, Projeto projeto) {
    public RequisitoNaoFuncionalResponseDTO(RequisitoNaoFuncional requisitoNaoFuncional){
        this(requisitoNaoFuncional.getId(), requisitoNaoFuncional.getTitulo(), requisitoNaoFuncional.getValor(), requisitoNaoFuncional.getProjeto());
    }
}

