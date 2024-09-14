package com.example.itera.domain.task;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.taskReview.TaskReview;
import com.example.itera.dto.task.TaskResponseDTO;
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

    public Task(TaskReview taskReviewData, Iteration iteration) {
        this.taskReview = taskReviewData;
        this.iteration = iteration;
    }

    public Task(TaskResponseDTO taskData) {
        this.id = taskData.id();
        this.title = taskData.title();
        this.priority = taskData.priority();
        this.details = taskData.details();
        this.complexity = taskData.complexity();
        this.effort = taskData.effort();
        this.sizeTask = taskData.sizeTask();
        this.startDate = taskData.startDate();
        this.endDate = taskData.endDate();
        this.orderTask = taskData.orderTask();
        this.listName = taskData.listName();
        this.taskType = taskData.taskType();
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

