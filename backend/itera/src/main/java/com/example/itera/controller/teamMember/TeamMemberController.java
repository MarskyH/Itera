package com.example.itera.controller.teamMember;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberRequestDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("teamMember")
public class TeamMemberController {
    @Autowired
    private TeamMemberRepository teamMemberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveTeamMember(@RequestBody TeamMemberRequestDTO data){
        User userData = userRepository.findById(data.user_id()).orElseThrow();
        Role roleData = roleRepository.findById(data.role_id()).orElseThrow();
        Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
        TeamMember teamData = new TeamMember(data.hourlyRate(), data.dedicatedHours(), userData, roleData, projectData);
        teamMemberRepository.save(teamData);
    }

    @GetMapping("{id}")
    public TeamMemberResponseDTO get(@PathVariable String id){
        TeamMember teamMember = teamMemberRepository.findById(id).orElseThrow();
        return new TeamMemberResponseDTO(teamMember);
    }

    @GetMapping("project/{id}")
    public List<TeamMemberResponseDTO> getAll(@PathVariable String id){
        List<TeamMember> teamMembers = teamMemberRepository.findAllByProjectId(id);
        List<TeamMemberResponseDTO> teamMemberList = new ArrayList<>();
        for (TeamMember teamMember : teamMembers) {
            teamMember.getUser();
            teamMember.getRole();
            teamMember.getProject();
            teamMemberList.add(new TeamMemberResponseDTO(teamMember.getId(), teamMember.getHourlyRate(), teamMember.getDedicatedHours(), teamMember.getUser(), teamMember.getRole(), teamMember.getProject()));
        }
        return teamMemberList;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> teamMember(@PathVariable String id) {
        try {
            teamMemberRepository.delete(teamMemberRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}