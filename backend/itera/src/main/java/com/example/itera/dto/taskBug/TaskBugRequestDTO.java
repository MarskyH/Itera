package com.example.itera.dto.taskBug;


public record TaskBugRequestDTO(String task_id, String requirement_id, Boolean checkBack, Boolean checkFront, Boolean checkTest) {
}

