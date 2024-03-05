package com.example.itera.domain.projeto;

import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;
import com.example.itera.domain.tarefa.Tarefa;
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
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nome;
    private Integer prazo;
    private Integer tempoIteracao;
    private String nomeCliente;

    @OneToOne(mappedBy = "projeto")
    private Equipe equipe;

    @OneToMany(mappedBy = "projeto")
    private List<Requisito> listaRequisitos;

    @OneToMany(mappedBy = "projeto")
    private List<Risco> listaRiscos;

    @OneToMany(mappedBy = "projeto")
    private List<Papel> listaPapeis;

    @OneToMany(mappedBy = "projeto")
    private List<RequisitoNaoFuncional> listaRequisitosNaoFuncionais;
}