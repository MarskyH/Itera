package com.example.itera.repository.nonFunctionalRequirementProject;


import com.example.itera.domain.nonFunctionalRequirementProject.NonFunctionalRequirementProject;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NonFunctionalRequirementProjectRepository extends JpaRepository<NonFunctionalRequirementProject, String>{

   @Query("SELECT n FROM NonFunctionalRequirementProject n WHERE n.project.id = :id")
    List<NonFunctionalRequirementProjectResponseDTO> findByProject(@Param("id") String id);

}



