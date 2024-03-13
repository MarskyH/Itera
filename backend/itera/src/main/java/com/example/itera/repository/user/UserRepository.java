package com.example.itera.repository.user;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);

    @Query("SELECT u FROM users u WHERE u.login = :username")
    User findByNome(@Param("username") String username);
}
