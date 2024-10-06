package com.example.itera.controller.activity;

import com.example.itera.domain.activity.Activity;
import com.example.itera.domain.project.Project;
import com.example.itera.domain. risk.Risk;
import com.example.itera.dto.activity.ActivityRequestDTO;
import com.example.itera.dto.activity.ActivityResponseDTO;
import com.example.itera.dto.risk.RiskResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.activity.ActivityRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository. risk.RiskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?>  saveActivity(@RequestBody ActivityRequestDTO data){
        Map<String, String> response = new HashMap<>();
        try{
            Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
            // Set the fetched Risk and Project entity in the Activity object
            Activity activityData = new Activity(data.title(), data.description(), data.type(), data.priority(), projectData);
            repository.save(activityData);
            response.put("project_id:", projectData.getId());
            response.put("activity_id:", activityData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
            // Save the Activity object
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ActivityResponseDTO getActivityById(@PathVariable String id) {
        Activity activity = repository.findById(id).orElseThrow();
        if (activity != null) {
            return new ActivityResponseDTO(activity);
        } else {
            return new ActivityResponseDTO(new Activity());
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Void> updateActivity(@PathVariable String id, @RequestBody ActivityRequestDTO data) {
        System.out.println(data);
        try {
            Activity activityExistente = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            Activity activityNova = new Activity(data);

            if(!data.title().isEmpty() && !data.title().isBlank()){
                activityExistente.setTitle(data.title());
            }
            if(!data.description().isEmpty() && !data.description().isBlank()){
                activityExistente.setDescription(data.description());
            }
            if(!data.type().isEmpty() && !data.type().isBlank()){
                activityExistente.setType(data.type());
            }
            if(!data.priority().isEmpty() && !data.priority().isBlank()){
                activityExistente.setPriority(data.priority());
            }

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


