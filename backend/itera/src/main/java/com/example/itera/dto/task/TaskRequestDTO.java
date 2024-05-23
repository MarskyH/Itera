package com.example.itera.dto.task;


import java.sql.Timestamp;

public record TaskRequestDTO(String title, String priority, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, String taskrequirement_id, String iteration_id) {
}

