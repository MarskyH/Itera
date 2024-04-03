package com.example.itera.controller.nonFunctionalRequirementProject;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.nonFunctionalRequirementProject.NonFunctionalRequirementProject;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectRequestDTO;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository.nonFunctionalRequirementProject.NonFunctionalRequirementProjectRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
@RequestMapping("nonFunctionalRequirementProject")
public class NonFunctionalRequirementProjectController {

    @Autowired
    NonFunctionalRequirementProjectRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    NonFunctionalRequirementRepository nonFunctionalRequirementRepository;

    /**
     * Endpoint responsável por cadastrar um requisito funcional.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir o requisito
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e do requesito criado.
     * @author Marcus Loureiro
     * @see NonFunctionalRequirementProjectRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveRequirement(@RequestBody List<NonFunctionalRequirementProjectRequestDTO> data) {
        System.out.println(data.toString());
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, String>> savedRequirements = new ArrayList<>();
            for (NonFunctionalRequirementProjectRequestDTO requirementDTO : data) {
                Project projectData = projectRepository.findById(requirementDTO.project_id()).orElseThrow(() -> new ValidationException("Project not found for ID: " + requirementDTO.project_id()));
                NonFunctionalRequirement nonFunctionalRequirementData = nonFunctionalRequirementRepository.findById(requirementDTO.nonfunctionalrequirement_id()).orElseThrow(() -> new ValidationException("Not found for ID: " + requirementDTO.nonfunctionalrequirement_id()));
                NonFunctionalRequirementProject nonFunctionalRequirementProject = new NonFunctionalRequirementProject(projectData, nonFunctionalRequirementData, requirementDTO.weight());
                repository.save(nonFunctionalRequirementProject);
                Map<String, String> savedRequirement = new HashMap<>();
                savedRequirement.put("project_id", projectData.getId());
                savedRequirement.put("requirement_id", nonFunctionalRequirementProject.getId());
                savedRequirement.put("wheight", String.valueOf(nonFunctionalRequirementProject.getWeight()));
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
     * Endpoint responsável por retornar um requisito específico, buscando pelo seu identificador.
     *
     * @param id Identificador único do requisito.
     * @return Requisito no formato RequirementResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso o requisito não seja encontrado.
     * @author Marcus Loureiro
     * @see RequirementResponseDTO
     * @since 23/03/2024
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public NonFunctionalRequirementProjectResponseDTO getRequirementById(@PathVariable String id) throws ResourceNotFoundException {
        NonFunctionalRequirementProject nonFunctionalRequirement = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new NonFunctionalRequirementProjectResponseDTO(nonFunctionalRequirement);
    }


    /**
     * Endpoint responsável por atualizar um requisito existente, identificado pelo seu ‘ID’.
     *
     * @param id   Identificador único do requisito a ser atualizado.
     * @param data Objeto contendo os dados do requisito a serem atualizados.
     * @return Resposta HTTP indicando o sucesso da operação ou informações sobre o erro ocorrido.
     * @throws EntityNotFoundException Exceção lançada caso o requisito não seja encontrado.
     * @author Marcus Loureiro
     * @since 23/03/2024
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequirement(@PathVariable String id, @RequestBody NonFunctionalRequirementProjectRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            NonFunctionalRequirementProject nonFunctionalRequirementProject = repository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.weight() != null) {
                nonFunctionalRequirementProject.setWeight(data.weight());
            }
            repository.save(nonFunctionalRequirementProject);
            response.put("data_id:", nonFunctionalRequirementProject.getId());
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



