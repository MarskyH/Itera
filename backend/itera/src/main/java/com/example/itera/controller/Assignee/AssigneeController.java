package com.example.itera.controller.Assignee;


import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
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
@RequestMapping("assignee")
public class AssigneeController {

    @Autowired
    AssigneeRepository assigneeRepository;

    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    TaskRepository taskRepository;

    /**
     * Endpoint responsável por cadastrar um requisito funcional.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir o responsável
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do responsável criado.
     * @author Marcus Loureiro
     * @see AssigneeRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveAssignee(@RequestBody AssigneeRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task taskData = taskRepository.findById(data.task_id()).orElseThrow();
            TeamMember memberData = teamMemberRepository.findById(data.member_id()).orElseThrow();
            Assignee assigneeData = new Assignee(
                    data.taskStep(),
                    data.deadline(),
                    memberData,
                    taskData
            );
            assigneeRepository.save(assigneeData);
            response.put("assignee_id:", assigneeData.getId());
            response.put("task_id:", taskData.getId());
            response.put("message:", ResponseType.SUCCESS_SAVE.getMessage());
            System.out.println("assignee_id: " + assigneeData.getId());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssigneeResponseDTO getAssigneeById(@PathVariable String id) throws ResourceNotFoundException {
        Assignee assignee = assigneeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new AssigneeResponseDTO(assignee);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignee(@PathVariable String id, @RequestBody AssigneeRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Assignee assignee = assigneeRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.member_id() != null) {
                TeamMember newMember = teamMemberRepository.findById(data.member_id()).orElseThrow(EntityNotFoundException::new);
                assignee.setTeamMember(newMember);
            }
            if(data.deadline() != null){
                assignee.setDeadline(data.deadline());
            }
            if (data.task_id() != null) {
                Task newTask = taskRepository.findById(data.task_id()).orElseThrow(EntityNotFoundException::new);
                assignee.setTask(newTask);
            }
            if (data.taskStep() != null) {
                assignee.setTaskStep(data.taskStep());
            }

            assigneeRepository.save(assignee);
            response.put("data_id:", assignee.getId());
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
    public ResponseEntity<Void> deleteRequirement(@PathVariable String id) {
        try {
            assigneeRepository.delete(assigneeRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


