package com.example.itera.controller.requirement;



import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requirement")
public class RequirementController {

    @Autowired
    private RequirementRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRequirement(@RequestBody RequirementRequestDTO data){
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
        Requirement requirementData = new Requirement(data.title(), data.details(), data.complexity(), data.priority(), data.effort(), data.sizeRequirement(), projectData);
        repository.save(requirementData);
    }

    @GetMapping("/project/{id}")
    public List<RequirementResponseDTO> getRequirementProject(@PathVariable String id){
        List<RequirementResponseDTO> requirementList = repository.findByProject(id).stream().toList();
        return requirementList;
    }

    @GetMapping("/{id}")
    public RequirementResponseDTO getRequirementById(@PathVariable String id) {
        Requirement requirement = repository.findById(id).orElseThrow();
        if (requirement != null) {
            return new RequirementResponseDTO(requirement);
        } else {
            return new RequirementResponseDTO(new Requirement());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRequirement(@PathVariable String id, @RequestBody RequirementRequestDTO data) {
        try {
            Requirement requirement = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            Requirement requirementNew = new Requirement(data);


            // Atualizar a entidade usando o construtor
            requirement = new Requirement(
                    requirement.getId(),
                    requirementNew.getTitle(),
                    requirementNew.getDetails(),
                    requirementNew.getComplexity(),
                    requirementNew.getPriority(),
                    requirementNew.getEffort(),
                    requirementNew.getSizeRequirement(),
                    requirement.getProject()
            );
            repository.save(requirement);
            return ResponseEntity.noContent().build();
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


