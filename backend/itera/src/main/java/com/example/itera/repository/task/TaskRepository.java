package com.example.itera.repository.task;

import com.example.itera.domain.task.Task;
import com.example.itera.dto.task.TaskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String>{

    @Query(value = "SELECT t FROM Task t WHERE t.iteration.id = :id")
    List<TaskResponseDTO> findByIteration(@Param("id") String id);

    @Query(value = "SELECT t FROM Task t WHERE t.iteration.id = :id and t.listName = :listName")
    List<TaskResponseDTO> findByIterationWithListName(@Param("id") String id, @Param("listName") String listName);
}



