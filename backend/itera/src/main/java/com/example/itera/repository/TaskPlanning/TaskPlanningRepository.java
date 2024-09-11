package com.example.itera.repository.TaskPlanning;

import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskPlanning.TaskPlanning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskPlanningRepository extends JpaRepository<TaskPlanning, String>{

}



