package com.example.itera.domain.equipe;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "equipe")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "equipe")
    private List<User> listaUsuarios;

    @ManyToOne
    private Projeto projeto;

    public Equipe(EquipeRequestDTO data){
        this.listaUsuarios = data.listaUsuarios();
        this.projeto = data.projeto();
    }
}

