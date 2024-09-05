package com.example.itera.domain.pendency;

import com.example.itera.domain.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "pendency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pendency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "list_name_original")
    private String listNameOriginal;
    @Column(name = "creation_date")
    private Timestamp creationDate;
    @Column(name = "end_date")
    private Timestamp endDate;
    @Column(name = "status")
    private Boolean status = true;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task;

    public Pendency(String title, String description, String listNameOriginal, Task task){
        this.title = title;
        this.description = description;
        this.listNameOriginal = listNameOriginal;
        this.task = task;
    }


}
