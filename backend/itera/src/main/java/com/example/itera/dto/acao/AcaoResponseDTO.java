package com.example.itera.dto.acao;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.risco.Risco;

public record AcaoResponseDTO(Long id, String titulo, String descricao, String tipo, Long risco_id) {
    public AcaoResponseDTO(Acao acao){
        this(acao.getId(), acao.getTitulo(), acao.getDescricao(), acao.getTipo(), acao.getRisco().getId());
    }
}

