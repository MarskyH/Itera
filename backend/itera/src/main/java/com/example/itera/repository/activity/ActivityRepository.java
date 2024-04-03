package com.example.itera.repository.activity;

import com.example.itera.domain.activity.Activity;
import com.example.itera.dto.activity.ActivityResponseDTO;
import com.example.itera.dto. risk.RiskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ActivityRepository extends JpaRepository<Activity, String>{


    @Query(value = "SELECT a FROM Activity a WHERE a.project.id = :id")
    List<ActivityResponseDTO> findByProjectId(@Param("id") String id);
}



