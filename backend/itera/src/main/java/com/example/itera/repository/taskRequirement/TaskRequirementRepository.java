package com.example.itera.repository.taskRequirement;

import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.taskType.TaskType;
import com.example.itera.dto.task.TaskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRequirementRepository extends JpaRepository<TaskRequirement, String> {
    @Query("SELECT SUM(CASE WHEN t.checkTest = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkBack = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkFront = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkRequirement = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkProject = true THEN 1 ELSE 0 END) " +
            "FROM TaskRequirement t WHERE t.id = :id")
    int getTotalChecksTrueById(@Param("id") String id);
}



