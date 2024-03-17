package com.example.itera.controller.user;

import com.example.itera.domain.activity.Activity;
import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.project.Project;
import com.example.itera.domain. risk.Risk;
import com.example.itera.domain.user.User;
import com.example.itera.dto.activity.ActivityRequestDTO;
import com.example.itera.dto.user.UserTeamRequestDTO;
import com.example.itera.repository.team.TeamRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.user.UserRepository;
import com.example.itera.dto.user.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAll(){
        List<UserResponseDTO> userList = userRepository.findAll().stream().map(UserResponseDTO::new).toList();
        return userList;
    }

    @PutMapping("/update/team")
    public ResponseEntity<Void> updateTeamUser(@RequestBody UserTeamRequestDTO data) {
        System.out.println("name user:" + data.nameUser());
        System.out.println("name project:" + data.nameProject());
        try {
            User dataUser = userRepository.findByNome(data.nameUser());
            Project dataProject = projectRepository.findByNome(data.nameProject());
            Team dataTeam = teamRepository.findByProject(dataProject.getId());
            Role dataRole = roleRepository.findById(data.role_id()).orElseThrow();
            dataUser.setTeam(dataTeam);
            dataUser.setHourlyRate(data.hourlyRate());
            dataUser.setDedicatedHours(data.dedicatedHours());
            dataUser.setRole(dataRole);
            userRepository.save(dataUser);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}