package com.example.itera.domain.acao;

import com.example.itera.domain.risco.Risco;
import com.example.itera.dto.acao.AcaoRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "acao_seq", sequenceName = "acao_seq", allocationSize = 1)
public class Acao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acao_seq")
    private Long id;
    private String titulo;
    private String descricao;
    private String tipo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "risco_id")
    private Risco risco;

    public Acao(AcaoRequestDTO data) {
        this.titulo = data.titulo();
        this.descricao = data.descricao();
        this.tipo = data.tipo();
        this.risco = data.risco();
    }

    public Acao(String titulo, String descricao, String tipo, Risco risco) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.risco = risco;
    }
}

