package com.example.itera.domain.requirement;

import com.example.itera.domain.project.Project;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "requirement")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String details;
    private String complexity;
    private String priority;
    private Integer effort;
    private Integer sizeRequirement;

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
        this.sizeRequirement = data.sizeRequirement();
    }

    public Requirement(String title, String details, String complexity, String priority, Integer effort, Integer sizeRequirement, Project project){
        this.title = title;
        this.details = details;
        this.complexity = complexity;
        this.priority = priority;
        this.effort = effort;
        this.sizeRequirement = sizeRequirement;
        this.project = project;
    }
}
