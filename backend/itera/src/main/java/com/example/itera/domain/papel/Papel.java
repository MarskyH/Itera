package com.example.itera.domain.papel;

import com.example.itera.domain.projeto.Projeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "papel")
@Entity(name = "papel")
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
  /*  @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;*/

}
