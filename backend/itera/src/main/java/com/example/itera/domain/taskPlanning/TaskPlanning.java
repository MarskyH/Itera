package com.example.itera.domain.taskPlanning;
import com.example.itera.domain.task.Task;
import com.example.itera.dto.project.BacklogRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
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

    @Column(columnDefinition = "TEXT")
    private String projectBacklog; // Armazena o BacklogRequestDTO como JSON

    @Column(columnDefinition = "TEXT")
    private String projectMembers; // Armazena o TeamMemberRequestDTO como JSON

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskPlanning(Task task) {
        this.task = task;
    }

    public TaskPlanning(Task task, Integer totalSize, Integer totalEffort, Double plannedSpeed, List<BacklogRequestDTO> projectBacklog, List<TeamMemberRequestDTO> projectMembers) {
        this.task = task;
        this.totalSize = totalSize;
        this.totalEffort = totalEffort;
        this.plannedSpeed = plannedSpeed;
        this.projectBacklog = convertToJson(projectBacklog);  // Converter lista para JSON
        this.projectMembers = convertToJson(projectMembers);  // Converter lista para JSON
    }

    // Método para converter a lista de objetos em JSON
    private <T> String convertToJson(List<T> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter lista para JSON", e);
        }
    }

    // Método para converter JSON de volta para uma lista de objetos
    private <T> List<T> convertFromJson(String json, Class<T[]> clazz) {
        try {
            T[] array = new ObjectMapper().readValue(json, clazz);
            return List.of(array); // Converte array para lista
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para lista", e);
        }
    }

    // Métodos auxiliares para uso posterior (ao carregar os dados):
    public List<BacklogRequestDTO> getProjectBacklogAsList() {
        return convertFromJson(this.projectBacklog, BacklogRequestDTO[].class);
    }

    public List<TeamMemberRequestDTO> getProjectMembersAsList() {
        return convertFromJson(this.projectMembers, TeamMemberRequestDTO[].class);
    }
}
