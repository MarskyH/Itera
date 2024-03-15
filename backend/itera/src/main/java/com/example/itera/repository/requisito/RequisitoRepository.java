package com.example.itera.repository.requisito;


import com.example.itera.domain.requisito.Requisito;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequisitoRepository extends JpaRepository<Requisito, Long>{

    @Query(value = "SELECT r FROM Requisito r WHERE r.projeto.id = :id")
    List<RequisitoResponseDTO> findByProjeto(@Param("id") Long id);


}



