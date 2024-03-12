package com.example.itera.repository.requisitoNaoFuncional;


import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RequisitoNaoFuncionalRepository extends JpaRepository<RequisitoNaoFuncional, Long>{

    @Query("SELECT r FROM RequisitoNaoFuncionalRepository r WHERE r.projeto_id = :id")
    RequisitoNaoFuncionalRepository findByProjeto(@Param("id") Integer id);


}



