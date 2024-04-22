package com.example.itera.domain.taskRequirement;
import com.example.itera.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "taskrequirement")
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
    private String effort;
    private String sizeTask;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
}