package com.example.itera.controller.nonFunctionalRequirement;


import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
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

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveNonFunctionalRequirement(@RequestBody NonFunctionalRequirementRequestDTO data){
        NonFunctionalRequirement nonFunctionalRequirementData = new NonFunctionalRequirement(data);
        repository.save(nonFunctionalRequirementData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<NonFunctionalRequirementResponseDTO> getAll(){
        List<NonFunctionalRequirementResponseDTO> nonFunctionalRequirementList = repository.findAll().stream().map(NonFunctionalRequirementResponseDTO::new).toList();
        return nonFunctionalRequirementList;
    }

    @GetMapping("/{id}")
    public NonFunctionalRequirementResponseDTO getRequirementNaoFuncionaById(@PathVariable String id) {
        NonFunctionalRequirement nonFunctionalRequirement = repository.findById(id).orElseThrow();
        if (nonFunctionalRequirement != null) {
            return new NonFunctionalRequirementResponseDTO(nonFunctionalRequirement);
        } else {
            return new NonFunctionalRequirementResponseDTO(new NonFunctionalRequirement());
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


