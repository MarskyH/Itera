package com.example.itera.controller.project;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.user.User;
import com.example.itera.dto.project.ProjectRequestDTO;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.infra.security.TokenService;
import com.example.itera.repository.activity.ActivityRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository. risk.RiskRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private ActivityRepository acapRepository;

    @Autowired
    private RequirementRepository requirementRepository;

    @Autowired
    private NonFunctionalRequirementRepository nonFunctionalRequirementRepository;


    @Autowired
    TokenService tokenService;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<?> saveProject(@RequestBody ProjectRequestDTO data){
        String username = tokenService.validateToken(SecurityContextHolder.getContext().getAuthentication().getName());
        User userData = userRepository.findByNome(username);
        Project projectData = new Project(data);
        projectData.setCreatedBy(userData.getId());
        projectRepository.save(projectData);

        // Criar um mapa com o ID do projeto
        Map<String, String> response = new HashMap<>();
        response.put("id", projectData.getId());

        return ResponseEntity.ok().body(response);
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<ProjectResponseDTO> getAll(){
        List<ProjectResponseDTO> projectList = projectRepository.findAll().stream().map(ProjectResponseDTO::new).toList();
        return projectList;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ProjectResponseDTO getProjectById(@PathVariable String id) {
        Project project = projectRepository.findById(id).orElseThrow();
        if (project != null) {
            return new ProjectResponseDTO(project);
        } else {
            return new ProjectResponseDTO(new Project());
        }
    }

    @GetMapping("/user/{id}")
    public List<ProjectResponseDTO> getProjectByCreatedBy(@PathVariable String id) {
        List<ProjectResponseDTO> projectList = projectRepository.findByCreatedBy(id).stream().toList();
        return projectList;
    }

    /*@GetMapping("/completo/{name}")
    public ProjectCompletResponseDTO getProjectCompleto(@PathVariable String name) {
        Project project = projectRepository.findByNome(name);
        if (project != null){
            List<RoleResponseDTO> listRole = roleRepository.findByProject(project.getId());
            //Team team = teamRepository.findByProject(project.getId());
           // List<UserResponseDTO> listaUsersTeam = teamRepository.findByUsersTeam(team.getId());
            List<RiskResponseDTO> listaRisks =  riskRepository.findByProject(project.getId());
            List<ActivityResponseDTO> listaAcoes = new ArrayList<>();

            for (RiskResponseDTO  risk : listaRisks) {
                // Para cada  risk, obtenha o ID do  risk
                String riskId =  risk.id();

                // Use o ID do  risk para pesquisar as ações correspondentes
                List<ActivityResponseDTO> acoesDoRisk = acapRepository.findByRiskId( riskId);

                // Adicione as ações encontradas à lista de ações
                listaAcoes.addAll(acoesDoRisk);
            }
            List<RequirementResponseDTO> listaRequirements = requirementRepository.findByProject(project.getId());
            List<NonFunctionalRequirementResponseDTO> listaRequirementsNaoFuncionais = nonFunctionalRequirementRepository.findByProject(project.getId());


            return new ProjectCompletResponseDTO(project, listRole,listaUsersTeam, listaRisks, listaAcoes, listaRequirements, listaRequirementsNaoFuncionais);
        }else{
            return new ProjectCompletResponseDTO(new Project(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }*/


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        try {
            projectRepository.delete(projectRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


