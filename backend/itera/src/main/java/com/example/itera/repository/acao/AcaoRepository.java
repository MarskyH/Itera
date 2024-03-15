package com.example.itera.repository.acao;

import com.example.itera.domain.acao.Acao;
import com.example.itera.dto.acao.AcaoResponseDTO;
import com.example.itera.dto.risco.RiscoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AcaoRepository extends JpaRepository<Acao, Long>{


    @Query(value = "SELECT a FROM Acao a WHERE a.risco.id = :id")
    List<AcaoResponseDTO> findByRiscoId(@Param("id") Long id);
}



