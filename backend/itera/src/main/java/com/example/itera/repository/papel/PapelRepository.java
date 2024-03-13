package com.example.itera.repository.papel;




import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PapelRepository extends JpaRepository<Papel, Long>{

    @Query(value = "SELECT p FROM Papel p WHERE p.projeto.id = :id")
    Equipe findByProjeto(@Param("id") Long id);

}



