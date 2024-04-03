package com.example.itera.controller.activity;

import com.example.itera.domain.activity.Activity;
import com.example.itera.domain.project.Project;
import com.example.itera.domain. risk.Risk;
import com.example.itera.dto.activity.ActivityRequestDTO;
import com.example.itera.dto.activity.ActivityResponseDTO;
import com.example.itera.dto.risk.RiskResponseDTO;
import com.example.itera.repository.activity.ActivityRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository. risk.RiskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

import java.util.List;

@RestController
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityRepository repository;

    @Autowired
    private RiskRepository  riskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveActivity(@RequestBody ActivityRequestDTO data){
        // Fetch the Risk entity from the database
        Risk risk =  riskRepository.findById(data.risk_id()).orElseThrow(() -> new EntityNotFoundException("Risk not found"));

        // Fetch the Risk and Project entity from the database
        Project project = projectRepository.findById(data.project_id()).orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Set the fetched Risk and Project entity in the Activity object
        Activity activityData = new Activity(data.title(), data.description(), data.type(), risk, project);

        // Save the Activity object
        repository.save(activityData);
    }

    @GetMapping("/project/{id}")
    public List<ActivityResponseDTO> getActionProject(@PathVariable String id){
        List<ActivityResponseDTO> activityList = repository.findByProjectId(id).stream().toList();
        return activityList;
    }

    @GetMapping("/{id}")
    public ActivityResponseDTO getActivityById(@PathVariable String id) {
        Activity activity = repository.findById(id).orElseThrow();
        if (activity != null) {
            return new ActivityResponseDTO(activity);
        } else {
            return new ActivityResponseDTO(new Activity());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateActivity(@PathVariable String id, @RequestBody ActivityRequestDTO data) {
        try {
            Activity activityExistente = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            Activity activityNova = new Activity(data);

            // Atualizar a entidade usando o construtor
            activityExistente = new Activity(
                    activityExistente.getId(),
                    activityNova.getTitle(),
                    activityNova.getDescription(),
                    activityNova.getType(),
                    activityExistente.getRisk(),
                    activityExistente.getProject()
            );

            repository.save(activityExistente);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace(); // ou logar o erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable String id) {
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


