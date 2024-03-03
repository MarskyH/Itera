package com.example.itera.domain.iteracao;

import com.example.itera.domain.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Table(name = "iteracao")
@Entity(name = "iteracao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Iteracao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date dataInicio;
    private Date dataFim;
   /* private List<Tarefa> listaTarefas;
    @OneToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;*/

}
