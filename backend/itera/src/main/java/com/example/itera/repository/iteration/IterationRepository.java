package com.example.itera.repository.iteration;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.dto.iteration.IterationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IterationRepository extends JpaRepository<Iteration, String>{

    @Query(value = "SELECT i FROM Iteration i WHERE i.status = :status and i.project.id = :id")
    List<IterationResponseDTO> findByProjectAndStatus(@Param("status") Boolean status, @Param("id") String id);

    @Query(value = "SELECT i FROM Iteration i WHERE i.project.id = :id")
    List<IterationResponseDTO> findByProject(@Param("id") String id);

}



