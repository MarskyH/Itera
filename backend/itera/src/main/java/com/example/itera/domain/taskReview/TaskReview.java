package com.example.itera.domain.taskReview;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.List;

@Entity
@Table(name = "task_review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer totalSize = 0;
    private Integer totalEffort = 0;
    private Double completedSpeed = 0.0;

    @Column(columnDefinition = "TEXT")
    private String iterationBacklog;
    @Column(columnDefinition = "TEXT")
    private String completedScope;
    @Column(columnDefinition = "TEXT")
    private String participatingMembers;
    private Boolean checkHumanResources = true;
    private Boolean checkScope = true;
    private Boolean checkSpeed = true;
    private Boolean checkRisks = true;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskReview(Task task) {
        this.task = task;
    }

    public TaskReview(Task task, Integer totalSize, Integer totalEffort, Double completedSpeed, List<BacklogResponseDTO> iterationBacklog, List<BacklogResponseDTO> completedScope, List<TeamMemberPlanningResponseDTO> participatingMembers,
                      Boolean checkHumanResources, Boolean checkScope, Boolean checkSpeed, Boolean checkRisks) {
        this.task = task;
        this.totalSize = totalSize;
        this.totalEffort = totalEffort;
        this.completedSpeed = completedSpeed;
        this.iterationBacklog = convertToJson(iterationBacklog);  // Converter lista para JSON
        this.completedScope = convertToJson(completedScope);  // Converter lista para JSON
        this.participatingMembers = convertToJson(participatingMembers);  // Converter lista para JSON
        this.checkHumanResources = checkHumanResources;
        this.checkScope = checkScope;
        this.checkSpeed = checkSpeed;
        this.checkRisks = checkRisks;
    }

    public TaskReview(Integer totalSize, Integer totalEffort, Double completedSpeed, List<BacklogResponseDTO> iterationBacklog, List<BacklogResponseDTO> completedScope, List<TeamMemberPlanningResponseDTO> participatingMembers,
                      Boolean checkHumanResources, Boolean checkScope, Boolean checkSpeed, Boolean checkRisks) {
        this.task = task;
        this.totalSize = totalSize;
        this.totalEffort = totalEffort;
        this.completedSpeed = completedSpeed;
        this.iterationBacklog = convertToJson(iterationBacklog);  // Converter lista para JSON
        this.completedScope = convertToJson(completedScope);  // Converter lista para JSON
        this.participatingMembers = convertToJson(participatingMembers);  // Converter lista para JSON
        this.checkHumanResources = checkHumanResources;
        this.checkScope = checkScope;
        this.checkSpeed = checkSpeed;
        this.checkRisks = checkRisks;
    }

    public TaskReview(TaskResponseDTO taskData, Object totalSize, Object totalEffort, Object completedSpeed, List<BacklogResponseDTO> listaBacklogIteration, List<BacklogResponseDTO> listaBacklogCompleted, Object participatingMembers, Object checkHumanResources, Object checkScope, Object checkSpeed, Object checkRisks) {
    }


    private <T> String convertToJson(List<T> list) {
        if (list == null) {
            return "[]";
        }
        list.forEach(Hibernate::initialize);
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter lista para JSON", e);
        }
    }


    private <T> List<T> convertFromJson(String json, Class<T[]> clazz) {
        if (json == null) {
            return List.of();
        }
        try {
            T[] array = new ObjectMapper().readValue(json, clazz);
            return List.of(array);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para lista", e);
        }
    }


    public List<BacklogResponseDTO> getIterationBacklogAsList() {
        return convertFromJson(this.iterationBacklog, BacklogResponseDTO[].class);
    }

    public List<BacklogResponseDTO> getCompletedScopelogAsList() {
        return convertFromJson(this.completedScope, BacklogResponseDTO[].class);
    }

    public List<TeamMemberPlanningResponseDTO> getParticipatingMembersAsList() {
        return convertFromJson(this.participatingMembers, TeamMemberPlanningResponseDTO[].class);
    }

    public String setIterationBacklogAsJson(List<BacklogResponseDTO> lista) {
        return convertToJson(lista);
    }

    public String setCompletedScopeAsJson(List<BacklogResponseDTO> lista) {
        return convertToJson(lista);
    }

    public String setParticipatingMembersAsJson(List<TeamMemberPlanningResponseDTO> lista) {
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


