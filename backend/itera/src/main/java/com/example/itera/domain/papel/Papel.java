package com.example.itera.domain.papel;

import com.example.itera.domain.projeto.Projeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "papel")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Papel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String funcao;
    private String habilidade;
    private String competencia;

    @ManyToOne
    private Projeto projeto;
}
