package com.example.itera.repository.requisitoNaoFuncional;


import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequisitoNaoFuncionalRepository extends JpaRepository<RequisitoNaoFuncional, Long>{

    @Query("SELECT r FROM RequisitoNaoFuncional r WHERE r.projeto.id = :id")
    List<RequisitoNaoFuncionalResponseDTO> findByProjeto(@Param("id") Long id);


}



