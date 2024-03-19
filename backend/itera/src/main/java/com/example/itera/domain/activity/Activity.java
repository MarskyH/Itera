package com.example.itera.domain.activity;

import com.example.itera.domain.risk.Risk;
import com.example.itera.domain.project.Project;
import com.example.itera.dto.activity.ActivityRequestDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String description;
    private String type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "risk_id")
    private Risk risk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Activity(ActivityRequestDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.type = data.type();
    }

    public Activity(String title, String description, String type, Risk risk, Project project) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.risk = risk;
        this.project = project;
    }
}

