package com.example.itera.domain.exposureDegree;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exposure_degree")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ExposureDegree {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
}

