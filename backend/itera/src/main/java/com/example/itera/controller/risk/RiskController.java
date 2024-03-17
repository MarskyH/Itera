package com.example.itera.controller. risk;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain. risk.Risk;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto. risk.RiskRequestDTO;
import com.example.itera.dto. risk.RiskResponseDTO;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository. risk.RiskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(" risk")
public class RiskController {

    @Autowired
    private RiskRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRisk(@RequestBody RiskRequestDTO data){
        Risk  riskData = new Risk(data);
        repository.save( riskData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RiskResponseDTO> getAll(){
        List<RiskResponseDTO>  riskList = repository.findAll().stream().map(RiskResponseDTO::new).toList();
        return  riskList;
    }

    @GetMapping("/{id}")
    public RiskResponseDTO getRiskById(@PathVariable String id) {
        Risk  risk = repository.findById(id).orElseThrow();
        if ( risk != null) {
            return new RiskResponseDTO( risk);
        } else {
            return new RiskResponseDTO(new Risk());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisk(@PathVariable String id) {
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


