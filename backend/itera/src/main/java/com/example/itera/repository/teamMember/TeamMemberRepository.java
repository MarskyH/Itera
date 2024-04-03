package com.example.itera.repository.teamMember;



import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String>{

    @Query(value = "SELECT t FROM TeamMember t WHERE t.project.id = :id")
    List<TeamMember> findAllByProjectId(@Param("id") String id);
}



