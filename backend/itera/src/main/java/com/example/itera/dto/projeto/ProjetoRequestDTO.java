package com.example.itera.dto.projeto;

import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;

import java.util.List;

public record ProjetoRequestDTO(String nome, Integer prazo, Integer tempoIteracao, String nomeCliente, String createdBy) {
}


