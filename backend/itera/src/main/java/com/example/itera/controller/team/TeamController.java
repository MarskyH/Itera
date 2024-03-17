package com.example.itera.controller.team;



import com.example.itera.domain.project.Project;
import com.example.itera.domain.team.Team;
import com.example.itera.dto.team.TeamRequestDTO;
import com.example.itera.dto.team.TeamResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.team.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("team")
public class TeamController {

    @Autowired
    private TeamRepository repository;

    @Autowired
    private ProjectRepository projectRepository;
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveTeam(@RequestBody TeamRequestDTO data){
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
        Team teamData = new Team(projectData);
        repository.save(teamData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<TeamResponseDTO> getAll(){
        List<TeamResponseDTO> teamList = repository.findAll().stream().map(TeamResponseDTO::new).toList();
        return teamList;
    }

    @GetMapping("/{id}")
    public TeamResponseDTO getTeamById(@PathVariable String id) {
        Team team = repository.findById(id).orElseThrow();
        if (team != null) {
            return new TeamResponseDTO(team);
        } else {
            return new TeamResponseDTO(new Team());
        }
    }

    @GetMapping("/users/{id}")
    public List<UserResponseDTO> getAllUsersTeamById(@PathVariable String id){
        List<UserResponseDTO> userList = repository.findByUsersTeam(id).stream().toList();
        return userList;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
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


