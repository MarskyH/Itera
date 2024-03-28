package com.example.itera.repository.nonFunctionalRequirementProject;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.nonFunctionalRequirementProject.NonFunctionalRequirementProject;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NonFunctionalRequirementProjectRepository extends JpaRepository<NonFunctionalRequirementProject, String>{

   /* @Query("SELECT r.weights FROM NonFunctionalRequirementProject r WHERE r.id = :id")
    List<RequirementResponseDTO> findByProject(@Param("id") String id);
*/
}



