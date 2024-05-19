package com.example.itera.domain.Assignee;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.task.TaskStep;
import com.example.itera.domain.taskType.TaskType;
import com.example.itera.domain.user.User;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "assignee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Assignee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "task_step")
    private String taskStep;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    public Assignee(String taskStep, User user, Task task){
        this.taskStep = taskStep;
        this.user = user;
        this.task = task;
    }


}
