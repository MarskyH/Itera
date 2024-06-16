package com.example.itera.dto.task;


import java.sql.Timestamp;

public record TaskRequestImprovementDTO(String title, String priority, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, String taskimprovement_id, String iteration_id) {
}

