package com.example.itera.repository.taskImprovement;

import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskImprovementRepository extends JpaRepository<TaskImprovement, String>{
    @Query("SELECT SUM(CASE WHEN t.checkTest = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkBack = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkFront = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkRequirement = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkProject = true THEN 1 ELSE 0 END) " +
            "FROM TaskImprovement t WHERE t.id = :id")
    int getTotalChecksTrueById(@Param("id") String id);
}



