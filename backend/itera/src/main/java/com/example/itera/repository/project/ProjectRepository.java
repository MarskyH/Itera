package com.example.itera.repository.project;




import com.example.itera.domain.project.Project;
import com.example.itera.dto.project.ProjectResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String>{

    @Query("SELECT p FROM Project p WHERE p.createdBy = :id")
    List<ProjectResponseDTO> findByCreatedBy(@Param("id") String id);

    @Query("SELECT p FROM Project p WHERE p.createdBy = :id ORDER BY p.lastAccessDate DESC")
    List<ProjectResponseDTO> findRecentProjects(@Param("id") String id);

    @Query("SELECT p FROM Project p")
    List<ProjectResponseDTO> findAllProjects();
}



