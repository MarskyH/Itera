package com.example.itera.controller.nonFunctionalRequirement;


import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("nonFunctionalRequirement")
public class NonFunctionalRequirementController {

    @Autowired
    private NonFunctionalRequirementRepository repository;

    @Autowired
    private ProjectRepository projectRepository;


    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveNonFunctionalRequirement(@RequestBody NonFunctionalRequirementRequestDTO data){
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
        NonFunctionalRequirement nonFunctionalRequirementData = new NonFunctionalRequirement(data.title(), data.valueRequirement(), projectData);
        repository.save(nonFunctionalRequirementData);
    }

    @GetMapping("/project/{id}")
    public List<NonFunctionalRequirementResponseDTO> getNonFunctionalRequiremenProject(@PathVariable String id){
        List<NonFunctionalRequirementResponseDTO> nonFunctionalRequiremenList = repository.findByProject(id).stream().toList();
        return nonFunctionalRequiremenList;
    }

    @GetMapping("/{id}")
    public NonFunctionalRequirementResponseDTO getNonFunctionalRequiremenById(@PathVariable String id) {
        NonFunctionalRequirement nonFunctionalRequirement = repository.findById(id).orElseThrow();
        if (nonFunctionalRequirement != null) {
            return new NonFunctionalRequirementResponseDTO(nonFunctionalRequirement);
        } else {
            return new NonFunctionalRequirementResponseDTO(new NonFunctionalRequirement());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNonFunctionalRequiremen(@PathVariable String id, @RequestBody NonFunctionalRequirementRequestDTO data) {
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
            return ResponseEntity.noContent().build();
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


