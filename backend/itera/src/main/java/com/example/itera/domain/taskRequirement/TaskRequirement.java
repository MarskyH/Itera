package com.example.itera.domain.taskRequirement;
import com.example.itera.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "task_requirement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String details;
    private String complexity;
    private Integer effort;
    private Integer sizeTask;
    private Boolean checkProject;
    private Boolean checkRequirement;
    private Boolean checkFront;
    private Boolean checkBack;
    private Boolean checkTest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskRequirement(String details, String complexity, Integer sizeTask, Integer effort, Task task) {
        this.details = details;
        this.complexity = complexity;
        this.sizeTask = sizeTask;
        this.effort = effort;
        this.task = task;
    }
}