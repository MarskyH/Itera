package com.example.itera.dto.task;


import java.sql.Timestamp;

public record TaskRequestBugDTO(String title, String priority, Timestamp startDate, Timestamp endDate, Integer orderTask, String listName, String taskType, String taskbug_id, String iteration_id) {
}

