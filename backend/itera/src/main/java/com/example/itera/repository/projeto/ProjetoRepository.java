package com.example.itera.repository.projeto;




import com.example.itera.domain.projeto.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjetoRepository extends JpaRepository<Projeto, Long>{

    @Query("SELECT p FROM Projeto p WHERE p.nome = :nome")
    Projeto findByNome(@Param("nome") String nome);

}



