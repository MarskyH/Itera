package com.example.itera.domain.categoria;


import com.example.itera.domain.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "categoria")
@Entity(name = "categoria")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String prioridade;
    private Date dataIncio;
    private Date dataFim;
   /* @OneToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;*/
}
