package com.example.itera.dto.project;




import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.project.Project;

import java.sql.Timestamp;


public record BacklogResponseDTO(String id, Integer orderRequirement, String title, String priority, Integer progressiveBar, Boolean checkCancelled) {

}


