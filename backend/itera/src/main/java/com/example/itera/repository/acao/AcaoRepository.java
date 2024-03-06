package com.example.itera.repository.acao;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AcaoRepository extends JpaRepository<Acao, Long>{

}



