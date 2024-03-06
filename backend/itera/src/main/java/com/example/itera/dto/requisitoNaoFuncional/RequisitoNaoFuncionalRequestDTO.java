package com.example.itera.dto.requisitoNaoFuncional;

import com.example.itera.domain.projeto.Projeto;

public record RequisitoNaoFuncionalRequestDTO(String nome, String titulo, String detalhamento, String complexidade, String prioridade,
                                              Integer esforco, Integer tamanho, Projeto projeto) {
}


