package com.example.itera.controller. risk;


import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.project.Project;
import com.example.itera.domain. risk.Risk;
import com.example.itera.domain.role.Role;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementRequestDTO;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto. risk.RiskRequestDTO;
import com.example.itera.dto. risk.RiskResponseDTO;
import com.example.itera.dto.role.RoleRequestDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository. risk.RiskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("risk")
public class RiskController {

    @Autowired
    private RiskRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRisk(@RequestBody RiskRequestDTO data){
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
        Risk riskData = new Risk(data.title(), data.effect(), data.probability(), data.impact(), data.exposureDegree(), projectData);
        repository.save(riskData);

    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RiskResponseDTO> getAll(){
        List<RiskResponseDTO>  riskList = repository.findAll().stream().map(RiskResponseDTO::new).toList();
        return  riskList;
    }

    @GetMapping("/project/{id}")
    public List<RiskResponseDTO> getRisksProject(@PathVariable String id){
        List<RiskResponseDTO> roleList = repository.findByProject(id).stream().toList();
        return roleList;
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRisk(@PathVariable String id, @RequestBody RiskRequestDTO data) {
        try {
            Risk risk = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            Risk riskNew = new Risk(data);


            // Atualizar a entidade usando o construtor
            risk = new Risk(
                    risk.getId(),
                    riskNew.getTitle(),
                    riskNew.getEffect(),
                    riskNew.getProbability(),
                    riskNew.getImpact(),
                    riskNew.getExposureDegree(),
                    risk.getProject()
            );
            repository.save(risk);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


