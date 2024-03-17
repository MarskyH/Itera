package com.example.itera.controller.requirement;



import com.example.itera.domain.requirement.Requirement;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
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

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRequirement(@RequestBody RequirementRequestDTO data){
        Requirement requirementData = new Requirement(data);
        repository.save(requirementData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RequirementResponseDTO> getAll(){
        List<RequirementResponseDTO> requirementList = repository.findAll().stream().map(RequirementResponseDTO::new).toList();
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


