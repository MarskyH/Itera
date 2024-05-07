package com.example.itera.domain.project;

import com.example.itera.dto.project.ProjectRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private Integer deadline;
    private Integer iterationTime;
    private String workHours;
    private String clientName;
    private String createdBy;
    private Timestamp creationDate;
    private Timestamp modificationDate;
    private Timestamp lastAccessDate;

    public Project(ProjectRequestDTO data){
        this.name = data.name();
        this.deadline = data.deadline();
        this.iterationTime = data.iterationTime();
        this.workHours = data.workHours();
        this.clientName = data.clientName();
    }
}