package com.example.itera.repository.requisito;


import com.example.itera.domain.requisito.Requisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequisitoRepository extends JpaRepository<Requisito, Long>{

    @Query("SELECT r FROM Requisito r WHERE r.projeto_id = :id")
    Requisito findByProjeto(@Param("id") Integer id);


}



