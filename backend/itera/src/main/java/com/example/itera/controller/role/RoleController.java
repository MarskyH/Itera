package com.example.itera.controller.role;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.dto.team.TeamRequestDTO;
import com.example.itera.dto.team.TeamResponseDTO;
import com.example.itera.dto.role.RoleRequestDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRole(@RequestBody RoleRequestDTO data){
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();

        Role roleData = new Role(data.function(), data.skill(), data.competency(), projectData);
        repository.save(roleData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RoleResponseDTO> getAll(){
        List<RoleResponseDTO> roleList = repository.findAll().stream().map(RoleResponseDTO::new).toList();
        return roleList;
    }


    @GetMapping("/{id}")
    public RoleResponseDTO getRoleById(@PathVariable String id) {
        Role role = repository.findById(id).orElseThrow();
        if (role != null) {
            return new RoleResponseDTO(role);
        } else {
            return new RoleResponseDTO(new Role());
        }
    }

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


