package com.example.itera.domain.risco;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.risco.RiscoRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "risco")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Risco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private String efeito;
    private String probabilidade;
    private String impacto;
    private String grauExposicao;

    @OneToOne
    private Acao acao;

    @ManyToOne
    private Projeto projeto;
    public Risco(RiscoRequestDTO data){
        this.titulo = data.titulo();
        this.efeito = data.efeito();
        this.probabilidade = data.probabilidade();
        this.impacto = data.impacto();
        this.grauExposicao = data.grauExposicao();
        this.projeto = data.projeto();

    }
}

