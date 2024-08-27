package com.example.itera.repository.pendency;

import com.example.itera.domain.pendency.Pendency;
import com.example.itera.domain.task.Task;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PendencyRepository extends JpaRepository<Pendency, String>{

    @Query(value = "SELECT p FROM Pendency p WHERE p.task.id = :id")
    List<PendencyResponseDTO> findByTask(@Param("id") String id);

    @Query(value = "SELECT p FROM Pendency p WHERE p.task.id = :id AND p.status = true")
    List<PendencyResponseDTO> findByAllTrue(@Param("id") String id);
}



