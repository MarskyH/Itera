package com.example.itera.dto.task;


import java.sql.Timestamp;

public record TaskCheckRequestDTO(Boolean checkProject, Boolean checkRequirement, Boolean checkFront, Boolean checkBack,
                                  Boolean checkTest) {
}

