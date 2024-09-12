package com.example.itera.domain.taskPlanning;
import com.example.itera.domain.task.Task;
import com.example.itera.dto.project.BacklogRequestDTO;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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
    private Integer totalSize = 0;
    private Integer totalEffort = 0;
    private Double plannedSpeed = 0.0;

    @Column(columnDefinition = "TEXT")
    private String projectBacklog;

    @Column(columnDefinition = "TEXT")
    private String projectMembers;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskPlanning(Task task) {
        this.task = task;
    }

    public TaskPlanning(Task task, Integer totalSize, Integer totalEffort, Double plannedSpeed, List<BacklogResponseDTO> projectBacklog, List<TeamMemberPlanningResponseDTO> projectMembers) {
        this.task = task;
        this.totalSize = totalSize;
        this.totalEffort = totalEffort;
        this.plannedSpeed = plannedSpeed;
        this.projectBacklog = convertToJson(projectBacklog);  // Converter lista para JSON
        this.projectMembers = convertToJson(projectMembers);  // Converter lista para JSON
    }

    // Método para converter a lista de objetos em JSON
    private <T> String convertToJson(List<T> list) {
        list.forEach(Hibernate::initialize);
        System.out.println(list);
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
    public List<BacklogResponseDTO> getProjectBacklogAsList() {
        return convertFromJson(this.projectBacklog, BacklogResponseDTO[].class);
    }

    public List<TeamMemberPlanningResponseDTO> getProjectMembersAsList() {
        return convertFromJson(this.projectMembers, TeamMemberPlanningResponseDTO[].class);
    }

    public String setProjectBacklogAsJson(List<BacklogResponseDTO> lista) {
        return convertToJson(lista);
    }

    public String setProjectMembersAsJson(List<TeamMemberPlanningResponseDTO> lista) {
        return convertToJson(lista);
    }

    public Integer getEffortCalculation(List<BacklogResponseDTO> lista){
        int effort = 0;
        for(BacklogResponseDTO item : lista){
            effort += item.effort();
        }
        return effort;
    }

    public Integer getSizeCalculation(List<BacklogResponseDTO> lista){
        int size = 0;
        for(BacklogResponseDTO item : lista){
            size += item.sizeRequirement();
        }
        return size;
    }
}


