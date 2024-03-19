package com.example.itera.domain.nonFunctionalRequirement;

import com.example.itera.domain.project.Project;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "nonfunctionalrequirement")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class NonFunctionalRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private Integer valueRequirement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    public NonFunctionalRequirement(NonFunctionalRequirementRequestDTO data){
        this.title = data.title();
        this.valueRequirement = data.valueRequirement();
    }

    public NonFunctionalRequirement(String title, Integer valueRequirement, Project project) {
        this.title = title;
        this.valueRequirement = valueRequirement;
        this.project = project;
    }
}

