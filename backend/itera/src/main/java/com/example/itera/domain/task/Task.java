package com.example.itera.domain.task;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.taskReview.TaskReview;
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
    private String sizeTask;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer progressiveBar = 0;
    private Integer orderTask;
    private String listName;
    private Boolean checkCancelled = false;
    private String  detailsCancelled;
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

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_planning_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskPlanning taskPlanning = null;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "task_review_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private TaskReview taskReview = null;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "iteration_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Iteration iteration;

    public Task(String title, String priority, String details, String complexity, String effort, String sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskRequirement taskRequirementData, Iteration iterationData) {
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

    public Task(String title, String priority, String details, String complexity, String effort, String sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskImprovement taskImprovementData, Iteration iterationData) {
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

    public Task(String title, String priority, String details, String complexity, String effort, String sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskBug taskBugData, Iteration iterationData) {
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

    public Task(String title, String priority, String details, String complexity, String effort, String sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, TaskRequirement taskRequirementData, TaskImprovement taskImprovementData,TaskBug taskBugData, TaskPlanning taskPlanningData, TaskReview taskReviewData, Iteration iterationData) {
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
        this.taskImprovement = taskImprovementData;
        this.taskBug = taskBugData;
        this.taskPlanning = taskPlanningData;
        this.taskReview = taskReviewData;
        this.iteration = iterationData;
    }

    public Task(Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10, Object o11, Object o12, Object o13, Object o14, TaskReview taskReview) {
    }

    public Boolean updateTaskListName(String oldListName, String newListName) {
        if(oldListName != null && newListName != null){
            return switch (oldListName) {
                case "A fazer" -> newListName.equals("Fazendo") || newListName.equals("Cancelado");
                case "Pendente" -> newListName.equals("Fazendo") || newListName.equals("Cancelado");
                case "Fazendo" ->
                        newListName.equals("Feito") || newListName.equals("Cancelado") || newListName.equals("Pendente");
                default -> false;
            };
        }else{
            return false;
        }
    }

    public  String getSizeRequirement(String sizeRequirement) {
        if (sizeRequirement != null){
            return switch (sizeRequirement) {
                case "5", "Pequeno" -> "Pequeno";
                case "10", "Médio" -> "Médio";
                case "15", "Grande" -> "Grande";
                default -> "0";
            };
        }else{
            return "";
        }
    }
}

