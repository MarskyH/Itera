package com.example.itera.domain. risk;

import com.example.itera.domain.activity.Activity;
import com.example.itera.domain.project.Project;
import com.example.itera.dto. risk.RiskRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = " risk")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String effect;
    private String probability;
    private String impact;
    private String exposureDegree;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;
    public Risk(RiskRequestDTO data){
        this.title = data.title();
        this.effect = data.effect();
        this.probability = data.probability();
        this.impact = data.impact();
        this.exposureDegree = data.exposureDegree();
    }

    public Risk(String title, String effect, String probability, String impact, String exposureDegree, Project project){
        this.title = title;
        this.effect = effect;
        this.probability = probability;
        this.impact = impact;
        this.exposureDegree = exposureDegree;
        this.project = project;
    }
}

