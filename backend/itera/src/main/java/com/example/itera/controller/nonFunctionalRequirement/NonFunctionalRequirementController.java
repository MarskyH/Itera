package com.example.itera.controller.nonFunctionalRequirement;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
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
@RequestMapping("nonFunctionalRequirement")
public class NonFunctionalRequirementController {

    @Autowired
    NonFunctionalRequirementRepository repository;

    @Autowired
    ObjectMapper objectMapper;


    @GetMapping
    public List<NonFunctionalRequirementResponseDTO> getAll(){
        return repository.findAll().stream().map(NonFunctionalRequirementResponseDTO::new).toList();
    }

    private NonFunctionalRequirementResponseDTO mapToResponseDTO(NonFunctionalRequirement requirement) {
        try {
            JsonNode weightsJson = objectMapper.readTree(requirement.getWeights());
            return new NonFunctionalRequirementResponseDTO(
                    requirement.getId(),
                    requirement.getTitle(),
                    requirement.getDescription(),
                    requirement.getWeightsJson(),
                    requirement.getMultiple()
            );
        } catch (IOException e) {
            // Handle exception appropriately
            e.printStackTrace();
            return null;
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

    @GetMapping("/{id}/weights")
    public JsonNode getNonFunctionalRequiremenWeightsById(@PathVariable String id) throws ResourceNotFoundException {
        return convertWeightsToNode(repository.findWeightsById(id));
    }

    private static JsonNode convertWeightsToNode(String weights) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(weights);
        } catch (JsonProcessingException e) {
            // Trate a exceção de acordo com a necessidade
            e.printStackTrace();
            return null;
        }
    }






}


