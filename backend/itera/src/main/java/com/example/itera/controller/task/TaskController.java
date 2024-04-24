package com.example.itera.controller.task;


import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.user.User;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.task.TaskRequestDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.dto.task.TaskTaskRequirementRequestDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementRequestDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.taskRequirement.TaskRequirementRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsável por fornecer endpoints para manipulação de requisitos
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 21/04/2024
 */

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    IterationRepository iterationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskRequirementRepository taskRequirementRepository;



    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTask(@RequestBody TaskRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Iteration iterationData = iterationRepository.findById(data.iteration_id()).orElseThrow();
            TaskRequirement taskRequirementData = new TaskRequirement();
            Task taskData = new Task(
                    data.title(),
                    data.priority(),
                    data.startDate(),
                    data.endDate(),
                    data.taskType(),
                    taskRequirementData,
                    iterationData
            );
            taskRepository.save(taskData);
            response.put("task_id:", taskData.getId());
            response.put("message:", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }

    @PostMapping("/type/requirement")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTaskRequirement(@RequestBody TaskTaskRequirementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            TaskRequestDTO dataTask = data.task();
            TaskRequirementRequestDTO dataTaskRequirement = data.taskRequirement();

            Iteration iterationData = iterationRepository.findById(dataTask.iteration_id()).orElseThrow();
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
                    dataTask.startDate(),
                    dataTask.endDate(),
                    data.task().taskType(),
                    null, // Deixe a associação com TaskRequirement como null por enquanto
                    iterationData
            );
            taskRepository.save(taskData); // Salve a Task primeiro

            TaskRequirement taskRequirementData = new TaskRequirement(
                    dataTaskRequirement.details(),
                    dataTaskRequirement.complexity(),
                    dataTaskRequirement.sizeTask(),
                    dataTaskRequirement.effort(),
                    taskData // Associe o TaskRequirement à Task salva
            );
            taskRequirementRepository.save(taskRequirementData); // Agora você pode salvar o TaskRequirement

            taskData.setTaskRequirement(taskRequirementData);
            taskRepository.save(taskData);

            response.put("task_id", taskData.getId());
            response.put("taskRequirement_id", taskRequirementData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }



    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TaskResponseDTO getTaskById(@PathVariable String id) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new TaskResponseDTO(task);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody TaskRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.taskrequirement_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskrequirement_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }
            taskRepository.save(task);
            response.put("data_id:", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            taskRepository.delete(taskRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


