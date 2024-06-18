package com.example.itera.domain.taskImprovement;

import com.example.itera.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_improvement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskImprovement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Boolean checkProject = false;
    private Boolean checkRequirement = false;
    private Boolean checkFront = false;
    private Boolean checkBack = false;
    private Boolean checkTest = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskImprovement(Task task) {
        this.task = task;
    }
}