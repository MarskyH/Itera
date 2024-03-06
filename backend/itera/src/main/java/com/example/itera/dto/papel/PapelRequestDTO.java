package com.example.itera.dto.papel;

import com.example.itera.domain.projeto.Projeto;

public record PapelRequestDTO(String funcao, String habilidade, String competencia, Projeto projeto) {
}


