package com.example.itera.dto.projeto;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import com.example.itera.dto.papel.PapelResponseDTO;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalResponseDTO;
import com.example.itera.dto.risco.RiscoResponseDTO;

import java.util.List;

public record ProjetoCompletoResponseDTO(Long id, String nome, Integer prazo, Integer tempoIteracao, String nomeCliente, String createdBy,
                                         List<PapelResponseDTO> listaPapel, List<User> listaUsersEquipe, List<RiscoResponseDTO> listaRiscos,
                                         List<RequisitoResponseDTO> listaRequisitos, List<RequisitoNaoFuncionalResponseDTO> listaRequisitosNaoFuncionais) {
    public ProjetoCompletoResponseDTO(Projeto projeto, List<PapelResponseDTO> listaPapel, List<User> listaUsersEquipe,  List<RiscoResponseDTO> listaRiscos, List<RequisitoResponseDTO> listaRequisitos, List<RequisitoNaoFuncionalResponseDTO> listaRequisitosNaoFuncionais){

        this(projeto.getId(),projeto.getNome(), projeto.getPrazo(), projeto.getTempoIteracao(), projeto.getNomeCliente(), projeto.getCreatedBy(), listaPapel, listaUsersEquipe, listaRiscos, listaRequisitos, listaRequisitosNaoFuncionais);
    }
}



