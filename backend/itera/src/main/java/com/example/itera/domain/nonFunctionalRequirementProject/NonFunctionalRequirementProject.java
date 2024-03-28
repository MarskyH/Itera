package com.example.itera.domain.nonFunctionalRequirementProject;

import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.io.IOException;

@Entity
@Table(name = "nonfunctionalrequirementproject")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class NonFunctionalRequirementProject {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nonfunctionalrequirement_id", nullable = false)
    @JsonIgnore
    private NonFunctionalRequirement nonFunctionalRequirement;

    @Column(name = "weight_value", nullable = false)
    private Integer weight;

    public NonFunctionalRequirementProject(Project project, NonFunctionalRequirement nonFunctionalRequirement, Integer weight){
        this.project = project;
        this.nonFunctionalRequirement = nonFunctionalRequirement;
        this.weight = weight;
    }


}

