package com.example.itera.domain.requisito;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "requisito")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "requisito_seq", sequenceName = "requisito_seq", allocationSize = 1)
public class Requisito {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requisito_seq")
    private Long id;
    private String titulo;
    private String detalhamento;
    private String complexidade;
    private String prioridade;
    private Integer esforco;
    private Integer tamanho;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Projeto projeto;
    public Requisito(RequisitoRequestDTO data){
        this.titulo = data.titulo();
        this.detalhamento = data.detalhamento();
        this.complexidade = data.complexidade();
        this.prioridade = data.prioridade();
        this.esforco = data.esforco();
        this.tamanho = data.tamanho();
        this.projeto = data.projeto();

    }
}
