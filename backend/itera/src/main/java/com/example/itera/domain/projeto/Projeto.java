package com.example.itera.domain.projeto;

import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;
import com.example.itera.domain.tarefa.Tarefa;
import com.example.itera.dto.papel.PapelRequestDTO;
import com.example.itera.dto.projeto.ProjetoRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "projeto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "projeto_seq", sequenceName = "projeto_seq", allocationSize = 1)
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projeto_seq")
    private Long id;
    private String nome;
    private Integer prazo;
    private Integer tempoIteracao;
    private String nomeCliente;

    public Projeto(ProjetoRequestDTO data){
        this.nome = data.nome();
        this.prazo = data.prazo();
        this.tempoIteracao = data.tempoIteracao();
        this.nomeCliente = data.nomeCliente();
    }
}