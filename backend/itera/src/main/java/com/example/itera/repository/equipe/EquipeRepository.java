package com.example.itera.repository.equipe;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe, Long>{

    @Query(value = "SELECT e FROM Equipe e WHERE e.projeto.id = :id")
    Equipe findByProjeto(@Param("id") Long id);

    @Query("SELECT u FROM users u WHERE u.equipe.id = :id")
    List<User> findByUsersEquipe(@Param("id") Long id);
}



