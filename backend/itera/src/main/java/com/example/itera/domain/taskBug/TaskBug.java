package com.example.itera.domain.taskBug;

import com.example.itera.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_bug")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskBug {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String details;
    private String complexity;
    private String effort;
    private String sizeTask;
    private Boolean checkFront;
    private Boolean checkBack;
    private Boolean checkTest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskBug(String details, String complexity, String sizeTask, String effort, Task task) {
        this.details = details;
        this.complexity = complexity;
        this.sizeTask = sizeTask;
        this.effort = effort;
        this.task = task;
    }
}