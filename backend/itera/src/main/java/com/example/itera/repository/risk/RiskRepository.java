package com.example.itera.repository. risk;



import com.example.itera.domain. risk.Risk;
import com.example.itera.dto. risk.RiskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RiskRepository extends JpaRepository<Risk, String>{

    @Query(value = "SELECT r FROM Risk r WHERE r.project.id = :id")
    List<RiskResponseDTO> findByProject(@Param("id") String id);


}



