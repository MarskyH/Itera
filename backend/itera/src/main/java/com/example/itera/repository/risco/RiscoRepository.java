package com.example.itera.repository.risco;



import com.example.itera.domain.risco.Risco;
import com.example.itera.dto.risco.RiscoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RiscoRepository extends JpaRepository<Risco, Long>{

    @Query(value = "SELECT r FROM Risco r WHERE r.projeto.id = :id")
    List<RiscoResponseDTO> findByProjeto(@Param("id") Long id);


}



