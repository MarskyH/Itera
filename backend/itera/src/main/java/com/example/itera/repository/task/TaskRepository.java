package com.example.itera.repository.task;

import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskType.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, String>{

}



