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
    private Boolean checkFront = false;
    private Boolean checkBack = false;
    private Boolean checkTest = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskBug(Task task) {
        this.task = task;
    }
}