package com.example.itera.domain.risco;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.risco.RiscoRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "risco")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "risco_seq", sequenceName = "risco_seq", allocationSize = 1)
public class Risco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "risco_seq")
    private Long id;
    private String titulo;
    private String efeito;
    private String probabilidade;
    private String impacto;
    private String grauExposicao;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
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

