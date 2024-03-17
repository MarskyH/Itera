package com.example.itera.repository.project;




import com.example.itera.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, String>{

    @Query("SELECT p FROM Project p WHERE p.name = :name")
    Project findByNome(@Param("name") String name);

}



