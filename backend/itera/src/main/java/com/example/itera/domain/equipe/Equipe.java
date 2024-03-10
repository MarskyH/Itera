package com.example.itera.domain.equipe;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "equipe")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "equipe_seq", sequenceName = "equipe_seq", allocationSize = 1)
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "equipe_seq")
    private Long id;

    @OneToMany(mappedBy = "equipe")
    private List<User> listaUsuarios;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "projeto_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Projeto projeto;

    public Equipe(EquipeRequestDTO data){
        this.listaUsuarios = data.listaUsuarios();
        this.projeto = data.projeto();
    }
}

