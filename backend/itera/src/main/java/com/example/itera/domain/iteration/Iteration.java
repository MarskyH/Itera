package com.example.itera.domain.iteration;

import com.example.itera.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "iteration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Iteration {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer number;
    @Column(name = "startdate")
    private Timestamp startDate;
    @Column(name = "enddate")
    private Timestamp endDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Project project;

    public Iteration(Integer number, Timestamp startDate, Timestamp endDate, Project projectData) {
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = projectData;
    }
}
