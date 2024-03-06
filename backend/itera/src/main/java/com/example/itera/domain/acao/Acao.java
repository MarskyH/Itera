package com.example.itera.domain.acao;

import com.example.itera.domain.risco.Risco;
import com.example.itera.dto.acao.AcaoRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "acao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Acao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;

    @OneToOne(mappedBy = "acao")
    private Risco risco;

    public Acao(AcaoRequestDTO data) {
        this.titulo = data.titulo();
        this.descricao = data.descricao();
        this.tipo = data.tipo();
        this.risco = data.risco();
    }
}

