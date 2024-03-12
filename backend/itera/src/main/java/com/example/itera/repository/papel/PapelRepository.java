package com.example.itera.repository.papel;




import com.example.itera.domain.papel.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PapelRepository extends JpaRepository<Papel, Long>{

    @Query("SELECT p FROM Papel p WHERE p.projeto_id = :id")
    Papel findByProjeto(@Param("id") Integer id);

}



