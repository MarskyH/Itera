package com.example.itera.dto.acao;

import com.example.itera.domain.risco.Risco;

public record AcaoRequestDTO(String titulo, String descricao, String tipo, Risco risco) {
}


