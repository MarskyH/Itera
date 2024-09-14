package com.example.itera.controller.iteration;


import com.example.itera.controller.task.TaskController;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskReview.TaskReview;
import com.example.itera.dto.iteration.IterationRequestDTO;
import com.example.itera.dto.iteration.IterationResponseDTO;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.task.TaskCompleteResponseDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.task.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * Responsável por fornecer endpoints para manipulação de iterações
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 22/04/2024
 */

@RestController
@RequestMapping("iteration")
public class IterationController {

    @Autowired
    IterationRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    TaskController taskController;
    @Autowired
    TaskRepository taskRepository;


    /**
     * Endpoint responsável por cadastrar um requisito funcional.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir a iteração
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e da iteração criada.
     * @author Marcus Loureiro
     * @see IterationRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveRequirement(@RequestBody IterationRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
            Iteration iterationData = new Iteration(
                    data.number(),
                    data.startDate(),
                    data.endDate(),
                    true,
                    projectData
            );
            repository.save(iterationData);
            response.put("project_id:", projectData.getId());
            response.put("iteration_id:", iterationData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    /**
     * Endpoint responsável por retornar uma iteração específica, buscando pelo seu identificador.
     *
     * @param id Identificador único da iteração.
     * @return Requisito no formato IterationResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso a iteração não seja encontrada.
     * @author Marcus Loureiro
     * @see IterationResponseDTO
     * @since 22/04/2024
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public IterationResponseDTO getIterationById(@PathVariable String id) throws ResourceNotFoundException {
        Iteration iteration = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new IterationResponseDTO(iteration);

    }

    /**
     * Endpoint responsável por retornar lista de requisitos por uma iteração específica, buscando pelo seu identificador.
     *
     * @param id Identificador único da iteração.
     * @return Lista de requisitos no formato RequirementResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso a iteração não seja encontrada.
     * @author Marcus Loureiro
     * @see RequirementResponseDTO
     * @since 13/06/2024
     */
    @GetMapping("/{id}/requirements")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequirementResponseDTO> getRequirementsByIterationId(@PathVariable String id){
        List<RequirementResponseDTO> requirements = requirementRepository.findByIteration(id);
        List<RequirementResponseDTO> requirementsDone = requirementRepository.findByIterationCompleted(id);
        Iteration iteration = repository.findById(id).orElseThrow();

        try{
            List<BacklogResponseDTO> listaBacklogIteration = new ArrayList<>();
            List<BacklogResponseDTO> listaBacklogCompleted = new ArrayList<>();
            for (RequirementResponseDTO requisito : requirements) {
                Requirement r = requirementRepository.findById(requisito.id()).orElseThrow();
                BacklogResponseDTO data = new BacklogResponseDTO(r.getId(), r.getOrderRequirement(), r.getTitle(), r.getPriority(), r.getProgressiveBar(), r.getEffort(), r.getSizeRequirement(), r.getCheckCancelled());
                listaBacklogIteration.add(data);
            }
            for (RequirementResponseDTO requisito : requirementsDone) {
                Requirement r = requirementRepository.findById(requisito.id()).orElseThrow();
                BacklogResponseDTO data = new BacklogResponseDTO(r.getId(), r.getOrderRequirement(), r.getTitle(), r.getPriority(), r.getProgressiveBar(), r.getEffort(), r.getSizeRequirement(), r.getCheckCancelled());
                listaBacklogCompleted.add(data);
            }
            TaskResponseDTO taskData = taskRepository.findByIterationTaskType(id, "Revisão");
            Task task = new Task(taskData);
            taskController.updateTaskById(taskData.id(), new TaskCompleteResponseDTO(new Task(
                    new TaskReview(0, 0, 0.0, listaBacklogIteration, listaBacklogCompleted, null, true, true, true, true)
                    ,iteration), null, null));
        }catch (Exception e){
            System.out.println("ERRO:" + e);
        }
        return requirements;
    }




    /**
     * Endpoint responsável por atualizar uma iteração existente, identificado pelo seu ‘ID’.
     *
     * @param id   Identificador único da iteração a ser atualizada.
     * @param data Objeto contendo os dados da  iteração a serem atualizados.
     * @return Resposta HTTP indicando o sucesso da operação ou informações sobre o erro ocorrido.
     * @throws EntityNotFoundException Exceção lançada caso a iteração não seja encontrada.
     * @author Marcus Loureiro
     * @since 22/04/2024
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIteration(@PathVariable String id, @RequestBody IterationRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Iteration iteration = repository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.number() != null) {
                iteration.setNumber(data.number());
            }
            if (data.startDate() != null) {
                iteration.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                iteration.setEndDate(data.endDate());
            }
            repository.save(iteration);
            response.put("data_id:", iteration.getId());
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
    public ResponseEntity<Void> deleteIteration(@PathVariable String id) {
        try {
            repository.delete(repository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


