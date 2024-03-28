package com.example.itera.controller.nonFunctionalRequirementProject;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.project.Project;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsável por fornecer endpoints para manipulação de requisito não funcioanis
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */

@RestController
@RequestMapping("nonFunctionalRequirement")
public class NonFunctionalRequirementProjectController {

    @Autowired
    NonFunctionalRequirementRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    /**
     * Endpoint responsável por cadastrar um risco.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir o requisito não funcional
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e do requisito não funcional criado.
     * @author Marcus Loureiro
     * @see NonFunctionalRequirementRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveNonFunctionalRequirements(@RequestBody List<NonFunctionalRequirementRequestDTO> data) {
        System.out.println(data.toString());
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, String>> savedRequirements = new ArrayList<>();
            for (NonFunctionalRequirementRequestDTO requirementDTO : data) {
                Project projectData = projectRepository.findById(requirementDTO.project_id())
                        .orElseThrow(() -> new ValidationException("Project not found for ID: " + requirementDTO.project_id()));
                NonFunctionalRequirement nonFunctionalRequirementData = new NonFunctionalRequirement(requirementDTO.title(), requirementDTO.valueRequirement(), projectData);
                repository.save(nonFunctionalRequirementData);
                Map<String, String> savedRequirement = new HashMap<>();
                savedRequirement.put("project_id", projectData.getId());
                savedRequirement.put("requirement_id", nonFunctionalRequirementData.getId());
                savedRequirements.add(savedRequirement);
            }
            response.put("saved_requirements", savedRequirements);
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
     * Endpoint responsável por retornar um requisito não funcional específico, buscando pelo seu identificador.
     *
     * @param id Identificador único do risco.
     * @return requisito não funcional no formato NonFunctionalRequirementResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso o risco não seja encontrado.
     * @author Marcus Loureiro
     * @see NonFunctionalRequirementResponseDTO
     * @since 23/03/2024
     */
    @GetMapping("/{id}")
    public NonFunctionalRequirementResponseDTO getNonFunctionalRequiremenById(@PathVariable String id) throws ResourceNotFoundException {
        NonFunctionalRequirement nonFunctionalRequirement = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new NonFunctionalRequirementResponseDTO(nonFunctionalRequirement);
    }

    /**
     * Endpoint responsável por atualizar um requisito não funcional existente, identificado pelo seu ‘ID’.
     *
     * @param id   Identificador único do requisito não funcional a ser atualizado.
     * @param data Objeto contendo os dados do requisito não funcional a serem atualizados.
     * @return Resposta HTTP indicando o sucesso da operação ou informações sobre o erro ocorrido.
     * @throws EntityNotFoundException Exceção lançada caso o risco não seja encontrado.
     * @author Marcus Loureiro
     * @since 23/03/2024
     */

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNonFunctionalRequiremen(@PathVariable String id, @RequestBody NonFunctionalRequirementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            NonFunctionalRequirement nonFunctionalRequirement = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            NonFunctionalRequirement nonFunctionalRequirementNew = new NonFunctionalRequirement(data);

            // Atualizar a entidade usando o construtor
            nonFunctionalRequirement = new NonFunctionalRequirement(
                    nonFunctionalRequirement.getId(),
                    nonFunctionalRequirementNew.getTitle(),
                    nonFunctionalRequirementNew.getValueRequirement(),
                    nonFunctionalRequirement.getProject()
            );
            repository.save(nonFunctionalRequirement);
            response.put("data_id:", nonFunctionalRequirement.getId());
            response.put("message", ResponseType.SUCCESS_UPDATE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNonFunctionalRequirement(@PathVariable String id) {
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


