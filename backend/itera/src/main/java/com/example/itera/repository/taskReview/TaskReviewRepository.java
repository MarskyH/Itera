package com.example.itera.repository.taskReview;

import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskReview.TaskReview;
import com.example.itera.dto.task.TaskResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskReviewRepository extends JpaRepository<TaskReview, String>{
}



