package com.example.itera.controller.teamMember;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
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
 * Responsável por fornecer endpoints para manipulação de membros de um projeto.
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */
@RestController
@RequestMapping("teamMember")
public class TeamMemberController {
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProjectRepository projectRepository;

    /**
     * Endpoint para cadastrar um papel.
     *
     * @param data Estrutura de dados contendo as informações necessárias para persistir o membro.
     * @throws EntityNotFoundException Exceção lançada caso o projeto não seja encontrado.
     * @since 23/03/2024
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTeamMember(@RequestBody TeamMemberRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            User userData = userRepository.findById(data.user_id()).orElseThrow();
            Role roleData = roleRepository.findById(data.role_id()).orElseThrow();
            Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
            TeamMember teamData = new TeamMember(data.hourlyRate(), data.dedicatedHours(), userData, roleData, projectData);
            teamMemberRepository.save(teamData);
            response.put("project_id:", projectData.getId());
            response.put("teamMember_id:", teamData.getId());
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
     * Endpoint para buscar um membro pelo seu identificador.
     *
     * @param id Identificador único da função.
     * @return TeamMemberResponseDTO representando o membro encontrada.
     * @since 23/03/2024
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TeamMemberResponseDTO get(@PathVariable String id) throws ResourceNotFoundException {
        TeamMember teamMember = teamMemberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new TeamMemberResponseDTO(teamMember);
    }

    /**
     * Endpoint para atualizar um membro da equipe existente.
     *
     * @param id Identificador único do membro da equipe a ser atualizado.
     * @param data Objeto contendo os dados do membro da equipe a serem atualizados.
     * @return ResponseEntity indicando o sucesso da operação.
     * @since 23/03/2024
     */

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> updateTeamMember(@PathVariable String id, @RequestBody TeamMemberRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            User userData = userRepository.findById(data.user_id()).orElseThrow();
            Role roleData = roleRepository.findById(data.role_id()).orElseThrow();
            TeamMember teamMember = teamMemberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if(data.hourlyRate() != null){
                teamMember.setHourlyRate(data.hourlyRate());
            }
            if(data.dedicatedHours() != null){
                teamMember.setDedicatedHours(data.dedicatedHours());
            }
            if(teamMember.getUser() != userData){
                teamMember.setUser(userData);
            }

            if(teamMember.getRole() != roleData){
                teamMember.setRole(roleData);
            }
            teamMemberRepository.save(teamMember);
            response.put("data_id:", teamMember.getId());
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
    public ResponseEntity<Void> teamMember(@PathVariable String id) {
        try {
            teamMemberRepository.delete(teamMemberRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}