package com.example.itera.domain.task;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    private String priority;
    private Timestamp startDate;
    private Timestamp endDate;

    @Column(name = "task_type")
    private String taskType;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_requirement_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskRequirement taskRequirement;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_improvement_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskImprovement taskImprovement;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_bug_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskBug taskBug;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iteration_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Iteration iteration;

    public Task(String title, String priority, Timestamp startDate, Timestamp endDate, String taskType, TaskRequirement taskRequirementData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskType = taskType;
        this.taskRequirement = taskRequirementData;
        this.iteration = iterationData;
    }

    public Task(String title, String priority, Timestamp startDate, Timestamp endDate, String taskType, TaskImprovement taskImprovementData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskType = taskType;
        this.taskImprovement = taskImprovementData;
        this.iteration = iterationData;
    }

    public Task(String title, String priority, Timestamp startDate, Timestamp endDate, String taskType, TaskBug taskBugData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.taskType = taskType;
        this.taskBug = taskBugData;
        this.iteration = iterationData;
    }
}
