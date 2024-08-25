package com.example.itera.dto.taskImprovement;


public record TaskImprovementRequestDTO(String task_id, String requirement_id, Boolean checkBack, Boolean checkFront, Boolean checkProject, Boolean checkRequirement, Boolean checkTest) {
}

