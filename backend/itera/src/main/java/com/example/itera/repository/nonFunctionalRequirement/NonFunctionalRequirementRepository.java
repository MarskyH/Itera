package com.example.itera.repository.nonFunctionalRequirement;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NonFunctionalRequirementRepository extends JpaRepository<NonFunctionalRequirement, String>{

    @Query("SELECT r FROM NonFunctionalRequirement r WHERE r.project.id = :id")
    List<NonFunctionalRequirementResponseDTO> findByProject(@Param("id") String id);


}



