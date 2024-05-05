package com.example.itera.repository.taskBug;

import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskBugRepository extends JpaRepository<TaskBug, String>{

}



