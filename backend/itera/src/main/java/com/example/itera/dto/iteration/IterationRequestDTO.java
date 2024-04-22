package com.example.itera.dto.iteration;


import java.sql.Timestamp;

public record IterationRequestDTO(Integer number, Timestamp startDate, Timestamp endDate, String project_id) {
}
