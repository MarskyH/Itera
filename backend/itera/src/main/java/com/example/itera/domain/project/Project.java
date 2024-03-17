package com.example.itera.domain.project;

import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;
import com.example.itera.dto.role.RoleRequestDTO;
import com.example.itera.dto.project.ProjectRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private Integer workHours;
    private String clientName;
    private String createdBy;

    public Project(ProjectRequestDTO data){
        this.name = data.name();
        this.deadline = data.deadline();
        this.iterationTime = data.iterationTime();
        this.workHours = data.workHours();
        this.clientName = data.clientName();
    }
}