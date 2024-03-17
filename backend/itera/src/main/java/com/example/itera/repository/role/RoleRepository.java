package com.example.itera.repository.role;




import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.dto.role.RoleResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String>{

    @Query(value = "SELECT p FROM Role p WHERE p.project.id = :id")
    List<RoleResponseDTO> findByProject(@Param("id") String id);

}



