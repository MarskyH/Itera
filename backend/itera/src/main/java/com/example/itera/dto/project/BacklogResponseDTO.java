package com.example.itera.dto.project;




import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.project.Project;

import java.sql.Timestamp;


public record BacklogResponseDTO(Integer id, String idRequirement, String title, String priority, Assignee assignee, String step, Integer progressiveBar) {

}


