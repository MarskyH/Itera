package com.example.itera.domain.team;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.user.User;
import com.example.itera.dto.team.TeamRequestDTO;
import com.example.itera.dto.team.TeamResponseDTO;
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
@Table(name = "team")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "team")
    private List<User> listaUsuarios;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;



    public Team(Project projectData) {
        this.project = projectData;
    }
}

