package com.example.itera.domain.tarefa;

import com.example.itera.domain.categoria.Categoria;
import com.example.itera.domain.iteracao.Iteracao;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Table(name = "tarefa")
@Entity(name = "tarefa")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private String prioridade;
    private String descricao;
   /* @OneToOne(mappedBy = "tarefa")
    private Iteracao iteracao;
    @OneToOne(mappedBy = "tarefa")
    private Categoria categoria;
    @OneToMany(mappedBy = "tarefa")
    private List<User> responsavel;
    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;*/

}
