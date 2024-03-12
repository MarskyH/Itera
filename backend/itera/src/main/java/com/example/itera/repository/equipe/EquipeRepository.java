package com.example.itera.repository.equipe;


import com.example.itera.domain.equipe.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipeRepository extends JpaRepository<Equipe, Long>{

    @Query("SELECT e FROM Equipe e WHERE e.projeto_id = :id")
    Equipe findByProjeto(@Param("id") Integer id);

}



