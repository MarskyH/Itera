package com.example.itera.domain.teamMember;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.user.User;
import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "team_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "dedicated_hours")
    private Integer dedicatedHours;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    public TeamMember(Double hourlyRate, Integer dedicatedHours, User userData, Role roleData, Project projectData) {
        this.hourlyRate = hourlyRate;
        this.dedicatedHours = dedicatedHours;
        this.user = userData;
        this.role = roleData;
        this.project = projectData;
    }

    public TeamMember(TeamMemberRequestDTO data) {
        this.hourlyRate = data.hourlyRate();
        this.dedicatedHours = data.dedicatedHours();
    }
}

