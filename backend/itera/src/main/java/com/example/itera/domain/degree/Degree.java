package com.example.itera.domain.degree;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "degree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
}

