package com.example.itera.domain.taskRetrospective;

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
@Table(name = "task_retrospective")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRetrospective {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(columnDefinition = "TEXT")
    private String participants;
    private String strengths;
    private String weaknesses;
    private String improvements;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskRetrospective(Task task) {
        this.task = task;
    }

    public TaskRetrospective(Task task, List<TeamMemberPlanningResponseDTO> participants, String strengths, String weaknesses, String improvements) {
        this.task = task;
        this.participants = convertToJson(participants);
        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.improvements = improvements;
    }

    public TaskRetrospective(List<TeamMemberPlanningResponseDTO> participants, String strengths, String weaknesses, String improvements) {
        this.participants = convertToJson(participants);
        this.strengths = strengths;
        this.weaknesses = weaknesses;
        this.improvements = improvements;
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

    public List<TeamMemberPlanningResponseDTO> getParticipantsAsList() {
        return convertFromJson(this.participants, TeamMemberPlanningResponseDTO[].class);
    }

    public String setParticipantsAsJson(List<TeamMemberPlanningResponseDTO> lista) {
        return convertToJson(lista);
    }
}


