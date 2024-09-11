package com.example.itera.domain.taskPlanning;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "task_planning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskPlanning {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer totalSize;
    private Integer totalEffort;
    private Double plannedSpeed;

    @Convert(converter = BacklogConverter.class)
    @Column(columnDefinition = "TEXT") // Armazena o JSON como texto no banco
    private String projectBacklog; // Armazena o BacklogResponseDTO como JSON

    @Convert(converter = TeamMemberConverter.class)
    @Column(columnDefinition = "TEXT")
    private String projectMembers; // Armazena o TeamMemberResponseDTO como JSON

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskPlanning(Task task) {
        this.task = task;
    }

    public TaskPlanning(Task task, Integer totalSize, Integer totalEffort, Double plannedSpeed, List<BacklogResponseDTO> projectBacklog, List<TeamMemberResponseDTO> projectMembers) {
        this.task = task;
        this.totalSize = totalSize;
        this.totalEffort = totalEffort;
        this.plannedSpeed = plannedSpeed;
        this.projectBacklog = convertBacklogToJson(projectBacklog);
        this.projectMembers = convertMembersToJson(projectMembers);
    }

    private String convertBacklogToJson(List<BacklogResponseDTO> backlog) {
        // Serializa a lista de BacklogResponseDTO como JSON
        try {
            return new ObjectMapper().writeValueAsString(backlog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Backlog para JSON", e);
        }
    }

    private String convertMembersToJson(List<TeamMemberResponseDTO> members) {
        // Serializa a lista de TeamMemberResponseDTO como JSON
        try {
            return new ObjectMapper().writeValueAsString(members);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Membros para JSON", e);
        }
    }
}
