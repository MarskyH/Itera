package com.example.itera.domain.taskType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasktype")
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

