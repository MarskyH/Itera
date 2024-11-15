package com.example.itera.repository.token;

import com.example.itera.domain.Password.PasswordResetToken;
import com.example.itera.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    @Query("SELECT p FROM PasswordResetToken p WHERE p.token = :token")
    Optional<PasswordResetToken> findByToken(String token);
}
