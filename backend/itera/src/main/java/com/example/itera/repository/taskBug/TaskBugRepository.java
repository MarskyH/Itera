package com.example.itera.repository.taskBug;

import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskBugRepository extends JpaRepository<TaskBug, String>{
    @Query("SELECT SUM(CASE WHEN t.checkTest = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkBack = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.checkFront = true THEN 1 ELSE 0 END) " +
            "FROM TaskBug t WHERE t.id = :id")
    int getTotalChecksTrueById(@Param("id") String id);
}



