package com.example.itera.domain.papel;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.papel.PapelRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "papel")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "papel_seq", sequenceName = "papel_seq", allocationSize = 1)
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "papel_seq")
    private Long id;
    private String funcao;
    private String habilidade;
    private String competencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Projeto projeto;
    public Papel(PapelRequestDTO data){
        this.funcao = data.funcao();
        this.habilidade = data.habilidade();
        this.competencia = data.competencia();
        this.projeto = data.projeto();
    }
}
