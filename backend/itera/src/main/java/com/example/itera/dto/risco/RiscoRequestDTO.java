package com.example.itera.dto.risco;

import com.example.itera.domain.projeto.Projeto;


public record RiscoRequestDTO(String titulo,  String efeito, String probabilidade, String impacto, String grauExposicao, Projeto projeto) {
}


