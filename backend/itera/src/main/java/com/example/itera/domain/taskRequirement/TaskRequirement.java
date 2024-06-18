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
    private Boolean checkProject = false;
    private Boolean checkRequirement = false;
    private Boolean checkFront = false;
    private Boolean checkBack = false;
    private Boolean checkTest = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public TaskRequirement(Task task) {
        this.task = task;
    }
}