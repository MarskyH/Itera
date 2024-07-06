package com.example.itera.domain.requirement;

import com.example.itera.domain.project.Project;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "requirement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id = "";
    private String requirementOriginal = "";
    private String title = "";
    private String details = "" ;
    private String complexity = "";
    private String priority = "";
    private Integer effort = 0;
    private Integer sizeRequirement = 0;
    private Integer orderRequirement = 0;
    private Integer contInteration = 0;
    private Integer progressiveBar = 0;
    private Boolean done = false;
    private String iterationId = null;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    public Requirement(RequirementRequestDTO data){
        this.title = data.title();
        this.details = data.details();
        this.complexity = data.complexity();
        this.priority = data.priority();
        this.effort = data.effort();
        this.sizeRequirement = getSizeRequirement(data.sizeRequirement());
        this.orderRequirement = data.orderRequirement();
        this.iterationId = data.iterationId();
    }

    public Requirement(String title, String details, String complexity, String priority, Integer effort, Integer sizeRequirement, Integer orderRequirement, String iterationId, Project project){
        this.title = title;
        this.details = details;
        this.complexity = complexity;
        this.priority = priority;
        this.effort = effort;
        this.sizeRequirement = sizeRequirement;
        this.orderRequirement = orderRequirement;
        this.iterationId = iterationId;
        this.project = project;
    }

   public int getSizeRequirement(String sizeRequirement) {
        switch (sizeRequirement) {
            case "Baixo":
                return 5;
            case "Médio":
                return 10;
            case "Grande":
                return 15;
            default:
                throw new IllegalArgumentException("Tamanho de Requisito inválido: " + sizeRequirement);
        }
    }
}
