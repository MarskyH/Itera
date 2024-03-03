package com.example.itera.domain.equipe;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "equipe")
@Entity(name = "equipe")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
   /* private List<User> listaUsuarios;
    @OneToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;*/
}
