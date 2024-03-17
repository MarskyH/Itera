package com.example.itera.repository.team;


import com.example.itera.domain.team.Team;
import com.example.itera.domain.user.User;
import com.example.itera.dto.user.UserResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String>{

    @Query(value = "SELECT e FROM Team e WHERE e.project.id = :id")
    Team findByProject(@Param("id") String id);

    @Query("SELECT u FROM users u WHERE u.team.id = :id")
    List<UserResponseDTO> findByUsersTeam(@Param("id") String id);
}



