package com.example.itera.repository.iteration;

import com.example.itera.domain.iteration.Iteration;
import com.example.itera.dto.iteration.IterationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IterationRepository extends JpaRepository<Iteration, String>{

    @Query(value = "SELECT i FROM Iteration i WHERE i.active = :active and i.project.id = :id")
    List<IterationResponseDTO> findByProjectAndActive(@Param("active") Boolean active, @Param("id") String id);

    @Query(value = "SELECT i FROM Iteration i WHERE i.project.id = :id")
    List<IterationResponseDTO> findByProject(@Param("id") String id);

}



