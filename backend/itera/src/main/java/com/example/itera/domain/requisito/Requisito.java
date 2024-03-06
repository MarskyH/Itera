package com.example.itera.domain.requisito;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requisito")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Requisito {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private String detalhamento;
    private String complexidade;
    private String prioridade;
    private Integer esforco;
    private Integer tamanho;

    @ManyToOne
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
