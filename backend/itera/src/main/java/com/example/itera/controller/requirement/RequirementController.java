package com.example.itera.controller.requirement;



import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
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
import java.util.List;
import java.util.Map;

/**
 * Responsável por fornecer endpoints para manipulação de requisitos
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */

@RestController
@RequestMapping("requirement")
public class RequirementController {

    @Autowired
    RequirementRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    IterationRepository iterationRepository;

    /**
     * Endpoint responsável por cadastrar um requisito funcional.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir o requisito
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e do requesito criado.
     * @author Marcus Loureiro
     * @see RequirementRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveRequirement(@RequestBody RequirementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        int order = 0;
        if(data.orderRequirement() == null){
            order = repository.findByProject(data.project_id()).size();
        }else{
            order = data.orderRequirement();
        }
        try {
            Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
            Requirement requirementData = new Requirement(
                    data.title(),
                    data.details(),
                    data.complexity(),
                    data.priority(),
                    data.effort(),
                    data.sizeRequirement(),
                    order,
                    data.iterationId(),
                    projectData
            );
            repository.save(requirementData);
            response.put("project_id:", projectData.getId());
            response.put("requirement_id:", requirementData.getId());
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
     * Endpoint responsável por cadastrar um requisito funcional.
     *
     * @param idRequirement identificador do requisito que irá ser iterado
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e do requesito criado.
     * @author Marcus Loureiro
     * @see RequirementRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */


    @PostMapping("{idRequirement}/iterated/{idIteration}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> iterationRequirement(@PathVariable String idRequirement, @PathVariable String idIteration) {
        System.out.println("ID ORIGINAL:" + idRequirement);
        Requirement original = repository.findById(idRequirement).orElseThrow();
        Map<String, String> response = new HashMap<>();
        try {
            Project projectData = projectRepository.findById(original.getProject().getId()).orElseThrow();
            Requirement requirementData = new Requirement(
                    original.getTitle(),
                    original.getDetails(),
                    original.getComplexity(),
                    original.getPriority(),
                    original.getEffort(),
                    original.getSizeRequirement(),
                    original.getOrderRequirement(),
                    idIteration,
                    projectData
            );

            requirementData.setRequirementOriginal(original.getId());
            requirementData.setContInteration(original.getContInteration()+1);
            requirementData.setTitle(original.getTitle() + "+" + requirementData.getContInteration());
            repository.save(requirementData);
            //original.setContInteration(original.getContInteration()+1);
            //repository.save(original);
            response.put("project_id:", projectData.getId());
            response.put("requirement_id:", requirementData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO:" + e);
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
    public RequirementResponseDTO getRequirementById(@PathVariable String id) throws ResourceNotFoundException {
        Requirement requirement = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new RequirementResponseDTO(requirement);
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
    public ResponseEntity<?> updateRequirement(@PathVariable String id, @RequestBody RequirementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Requirement requirement = repository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                requirement.setTitle(data.title());
            }
            if (data.details() != null) {
                requirement.setDetails(data.details());
            }
            if (data.complexity() != null) {
                requirement.setComplexity(data.complexity());
            }
            if (data.priority() != null) {
                requirement.setPriority(data.priority());
            }
            if (data.effort() != null) {
                requirement.setEffort(data.effort());
            }
            if (data.sizeRequirement() != null) {
                requirement.setSizeRequirement(data.sizeRequirement());
            }
            if (data.orderRequirement() != null) {
                requirement.setOrderRequirement(data.orderRequirement());
            }
            if (data.iterationId() != null) {
                requirement.setIterationId(data.iterationId());
            }
            repository.save(requirement);
            response.put("data_id:", requirement.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/list/iteration/{iterationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequirementResponseDTO> getRequirementByIteration(@PathVariable String iterationId){
        return repository.findByIteration(iterationId).stream().toList();
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


