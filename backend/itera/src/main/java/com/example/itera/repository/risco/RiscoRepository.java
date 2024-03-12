package com.example.itera.repository.risco;



import com.example.itera.domain.risco.Risco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RiscoRepository extends JpaRepository<Risco, Long>{

    @Query("SELECT r FROM Risco r WHERE r.projeto_id = :id")
    Risco findByProjeto(@Param("id") Integer id);


}



