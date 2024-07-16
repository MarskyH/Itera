package com.example.itera.repository.assignee;

import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.task.TaskStep;
import com.example.itera.domain.taskType.TaskType;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.iteration.IterationResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssigneeRepository extends JpaRepository<Assignee, String>{
    @Query(value = "SELECT a FROM Assignee a WHERE a.task.id = :id")
    List<AssigneeResponseDTO> findByTask(@Param("id") String id);

    @Query("SELECT a FROM Assignee a WHERE a.taskStep = :taskStep AND a.teamMember.id = :memberId")
    Assignee findByMemberIdStep(@Param("taskStep") TaskStep taskStep, @Param("memberId") String memberId);

}



