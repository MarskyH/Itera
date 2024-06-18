package com.example.itera.dto.task;


import java.sql.Timestamp;

public record TaskRequestRequirementDTO(String title, String priority,  String details, String complexity, String effort, Integer sizeTask, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, String taskrequirement_id, String iteration_id) {
}

