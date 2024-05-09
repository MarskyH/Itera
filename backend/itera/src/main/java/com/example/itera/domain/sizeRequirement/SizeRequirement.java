package com.example.itera.domain.sizeRequirement;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "size_requirement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SizeRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private Integer function_points;
}

