package com.example.itera.dto.degree;



import com.example.itera.domain.degree.Degree;

public record DegreeResponseDTO(String id, String name) {
    public DegreeResponseDTO(Degree degree){
        this(degree.getId(),  degree.getName());
    }



}

