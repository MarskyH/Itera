package com.example.itera.dto.requisito;

import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;

import java.util.List;

public record RequisitoRequestDTO(String nome, String titulo, String detalhamento, String complexidade, String prioridade,
                                  Integer esforco, Integer tamanho, Projeto projeto) {
}


