package com.example.itera.controller.role;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.dto.role.RoleRequestDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * Responsável por fornecer endpoints para manipulação de papéis.
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    RoleRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    /**
     * Endpoint para cadastrar um papel.
     *
     * @param data Estrutura de dados contendo as informações necessárias para persistir o papel.
     * @throws EntityNotFoundException Exceção lançada caso o projeto não seja encontrado.
     * @since 23/03/2024
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveRole(@RequestBody RoleRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow(EntityNotFoundException::new);
        Role roleData = new Role(data.function(), data.skill(), data.competency(), projectData);
            repository.save(roleData);
            response.put("project_id:", projectData.getId());
            response.put("role_id:", roleData.getId());
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
     * Endpoint para buscar um papel pelo seu identificador.
     *
     * @param id Identificador único da função.
     * @return RoleResponseDTO representando o papel encontrada.
     * @since 23/03/2024
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RoleResponseDTO getRoleById(@PathVariable String id) throws ResourceNotFoundException {
        Role role = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new RoleResponseDTO(role);
    }

    /**
     * Endpoint para atualizar um papel existente.
     *
     * @param id   Identificador único do papel a ser atualizado.
     * @param data Objeto contendo os dados do papel a serem atualizados.
     * @return ResponseEntity indicando o sucesso da operação.
     * @since 23/03/2024
     */
    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> updateRole(@PathVariable String id, @RequestBody RoleRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Role role = repository.findById(id).orElseThrow(EntityNotFoundException::new);

            if(data.function() != null){
                role.setFunction(data.function());
            }
            if(data.skill() != null){
                role.setSkill(data.skill());
            }
            if(data.competency() != null){
                role.setCompetency(data.competency());
            }
            repository.save(role);
            response.put("data_id:", role.getId());
            response.put("message", ResponseType.SUCCESS_UPDATE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para excluir um papel existente.
     *
     * @param id Identificador único da função a ser excluída.
     * @return ResponseEntity indicando o sucesso da exclusão.
     * @since 23/03/2024
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteARole(@PathVariable String id) {
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


