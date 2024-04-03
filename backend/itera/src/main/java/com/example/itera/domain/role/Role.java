package com.example.itera.domain.role;

import com.example.itera.domain.project.Project;
import com.example.itera.dto.role.RoleRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String function;
    private String skill;
    private String competency;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;
    public Role(RoleRequestDTO data){
        this.function = data.function();
        this.skill = data.skill();
        this.competency = data.competency();
    }

    public Role(String function, String skill, String competency, Project projectData) {
        this.function = function;
        this.skill = skill;
        this.competency = competency;
        this.project = projectData;
    }
}
