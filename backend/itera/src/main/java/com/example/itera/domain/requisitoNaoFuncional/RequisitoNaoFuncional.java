package com.example.itera.domain.requisitoNaoFuncional;

import com.example.itera.domain.projeto.Projeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "requisitonaofuncional")
@Entity(name = "requisitonaofuncional")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RequisitoNaoFuncional {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private Integer valor;

   /* @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;*/
}
