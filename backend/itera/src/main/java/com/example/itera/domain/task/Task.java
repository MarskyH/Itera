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
    private String details;
    private String complexity;
    private String effort;
    private Integer sizeTask;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer orderTask;
    private String listName;

    @Column(name = "task_type")
    private String taskType;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_requirement_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskRequirement taskRequirement = null;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_improvement_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskImprovement taskImprovement = null;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_bug_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskBug taskBug = null;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iteration_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Iteration iteration;

    public Task(String title, String priority, String details, String complexity, String effort, Integer sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskRequirement taskRequirementData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.details = details;
        this.complexity = complexity;
        this.effort = effort;
        this.sizeTask = sizeTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderTask = orderTask;
        this.listName = listName;
        this.taskType = taskType;
        this.taskRequirement = taskRequirementData;
        this.iteration = iterationData;
    }

    public Task(String title, String priority, String details, String complexity, String effort, Integer sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskImprovement taskImprovementData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.details = details;
        this.complexity = complexity;
        this.effort = effort;
        this.sizeTask = sizeTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderTask = orderTask;
        this.listName = listName;
        this.taskType = taskType;
        this.taskImprovement = taskImprovementData;
        this.iteration = iterationData;
    }

    public Task(String title, String priority, String details, String complexity, String effort, Integer sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskBug taskBugData, Iteration iterationData) {
        this.title = title;
        this.priority = priority;
        this.details = details;
        this.complexity = complexity;
        this.effort = effort;
        this.sizeTask = sizeTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.orderTask = orderTask;
        this.listName = listName;
        this.taskType = taskType;
        this.taskBug = taskBugData;
        this.iteration = iterationData;
    }

    public Boolean updateTaskListName(String oldListName, String newListName) {
        System.out.println(oldListName + " to " + newListName);
        return switch (oldListName) {
            case "A fazer" -> newListName.equals("Fazendo") || newListName.equals("Cancelado");
            case "Pendente" -> newListName.equals("Fazendo") || newListName.equals("Cancelado");
            case "Fazendo" ->
                    newListName.equals("Feito") || newListName.equals("Cancelado") || newListName.equals("Pendente");
            default -> false;
        };
    }
}

