package com.example.itera.controller.iteration;


import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.iteration.IterationRequestDTO;
import com.example.itera.dto.iteration.IterationResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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


