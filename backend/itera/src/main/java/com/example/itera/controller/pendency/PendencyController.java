package com.example.itera.controller.pendency;


import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.pendency.Pendency;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.pendency.PendencyRequestDTO;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.pendency.PendencyRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsável por fornecer endpoints para manipulação de pendências
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 14/07/2024
 */

@RestController
@RequestMapping("pendency")
public class PendencyController {

    @Autowired
    PendencyRepository pendencyRepository;

    @Autowired
    TaskRepository taskRepository;

    /**
     * Endpoint responsável por cadastrar uma pendência.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir a pendência
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ da pendência criada.
     * @author Marcus Loureiro
     * @see PendencyRequestDTO
     * @see ResponseType
     * @since 14/07/2024
     */


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> savePendency(@RequestBody PendencyRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        String listNameOriginal = "";
        try {
            Task taskData = taskRepository.findById(data.task_id()).orElseThrow();
            List<Pendency> listPendency = pendencyRepository.findByAllTrue(taskData.getId());
            if(listPendency.isEmpty()){
                listNameOriginal = taskData.getListName();
            }else{
                listNameOriginal = listPendency.getFirst().getListNameOriginal();
            }
            Pendency pendencyData = new Pendency(
                    data.title(),
                    data.description(),
                    listNameOriginal,
                    taskData
            );
            pendencyData.setCreationDate(new Timestamp(new Date().getTime()));
            pendencyData.setStatus(true);
            taskData.setListName("Pendente");
            taskRepository.save(taskData);
            pendencyRepository.save(pendencyData);
            response.put("pendency_id:", pendencyData.getId());
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


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PendencyResponseDTO getPendencyById(@PathVariable String id) throws ResourceNotFoundException {
        Pendency pendency = pendencyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new PendencyResponseDTO(pendency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePendency(@PathVariable String id, @RequestBody PendencyRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Pendency pendency = pendencyRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                pendency.setTitle(data.title());
            }
            if(data.description() != null){
                pendency.setDescription(data.description());
            }
            if (!data.status()) {
                pendency.setStatus(false);
                pendency.setEndDate(new Timestamp(new Date().getTime()));
                pendencyRepository.save(pendency);
                Task task = taskRepository.findById(pendency.getTask().getId()).orElseThrow(EntityNotFoundException::new);
                List<Pendency> listPendency = pendencyRepository.findByAllTrue(task.getId());
                if(listPendency.isEmpty()){
                    task.setListName(pendency.getListNameOriginal());
                    taskRepository.save(task);
                }
            }else{
                pendency.setStatus(true);
            }
            pendencyRepository.save(pendency);
            response.put("data_id:", pendency.getId());
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
    public ResponseEntity<Void> deletePendency(@PathVariable String id) {
        try {
            pendencyRepository.delete(pendencyRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


