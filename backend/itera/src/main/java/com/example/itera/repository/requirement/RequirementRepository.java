package com.example.itera.repository.requirement;


import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, String>{

    @Query(value = "SELECT r FROM Requirement r WHERE r.project.id = :id")
    List<RequirementResponseDTO> findByProject(@Param("id") String id);

    @Query(value = "SELECT r FROM Requirement r WHERE r.iterationId = :id ORDER BY r.orderRequirement ASC")
    List<RequirementResponseDTO> findByIteration(@Param("id") String id);

    @Query(value = "SELECT r FROM Requirement r WHERE r.title = :name")
    Requirement findByName(@Param("name") String name);

    @Query(value = "SELECT r FROM Requirement r WHERE r.title LIKE %:name%")
    List<RequirementResponseDTO> findByNameContaining(@Param("name") String name);


}



