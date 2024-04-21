package com.example.itera.domain.taskType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
}

