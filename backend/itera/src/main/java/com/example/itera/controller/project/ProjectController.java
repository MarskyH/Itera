package com.example.itera.controller.project;

import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.nonFunctionalRequirement.NonFunctionalRequirement;
import com.example.itera.domain.nonFunctionalRequirementProject.NonFunctionalRequirementProject;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.risk.Risk;
import com.example.itera.domain.role.Role;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.iteration.IterationResponseDTO;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectRequestDTO;
import com.example.itera.dto.nonFunctionalRequirementProject.NonFunctionalRequirementProjectResponseDTO;
import com.example.itera.dto.project.BacklogResponseDTO;
import com.example.itera.dto.project.ProjectWithJoinResponseDTO;
import com.example.itera.dto.role.RoleRequestDTO;
import com.example.itera.dto.task.TaskResponseDTO;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.nonFunctionalRequirement.NonFunctionalRequirementResponseDTO;
import com.example.itera.dto.project.ProjectRequestDTO;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.risk.RiskResponseDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.infra.security.TokenService;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.nonFunctionalRequirementProject.NonFunctionalRequirementProjectRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.nonFunctionalRequirement.NonFunctionalRequirementRepository;
import com.example.itera.repository. risk.RiskRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
* Responsável por fornecer um endpoint para criar um novo projeto.
*
* @author Marcus Loureiro
* @since 19/03/2024
* @version 1.0
* */


@SuppressWarnings("ALL")
@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RiskRepository riskRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    NonFunctionalRequirementProjectRepository nonFunctionalRequirementProjectRepository;

    @Autowired
    IterationRepository iterationRepository;

    TaskRepository taskRepository;

    @Autowired
    TokenService tokenService;


    /**
    * Endpoint responsável por cadastrar um projeto.
    *
    * @param data estrutura de dados contendo as informações necessárias para persistir o projeto
    * @return ResponseEntity confirmando a transação e retornando o id do projeto criado.
    * @author Marcus Loureiro
    * @see ProjectRequestDTO
    * @see ResponseType
    * @since 19/03/2024
    * */

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveProject(@RequestBody @Valid ProjectRequestDTO data){
        Map<String, String> response = new HashMap<>();
        try {
            String username = tokenService.validateToken(SecurityContextHolder.getContext().getAuthentication().getName());
            User userData = userRepository.findByNome(username);
            Project projectData = new Project(data);
            projectData.setCreatedBy(userData.getId());
            projectData.setCreationDate(new Timestamp(new Date().getTime()));
            projectData.setModificationDate(new Timestamp(new Date().getTime()));
            projectData.setLastAccessDate(new Timestamp(new Date().getTime()));
            projectRepository.save(projectData);
            response.put("id", projectData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }

    @PostMapping("/{id}/createIterations")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> createIterations(@PathVariable String id){
        Map<String, String> response = new HashMap<>();
        try {
            Project projectData = projectRepository.findById(id).orElseThrow();

            // Obtendo informações do projeto
            Integer deadline = projectData.getDeadline();
            Integer iterationTime = projectData.getIterationTime();

            // Calculando o número de iterações com base no prazo e no tempo de iteração
            Integer counterIteration = deadline / iterationTime;

            // Obtendo a data atual
            Timestamp currentDate = new Timestamp(new Date().getTime());

            // Definindo a data inicial como a data atual
            Timestamp startDate = currentDate;

            for (int i = 1; i <= counterIteration; i++) {
                // Calculando a data final como a data inicial + o tempo de iteração
                Timestamp endDate = new Timestamp(startDate.getTime() + (iterationTime * 24 * 60 * 60 * 1000));

                // Criando e salvando a iteração com as datas calculadas
                Iteration iterationData = new Iteration(i, startDate, endDate, projectData);
                iterationRepository.save(iterationData);

                // Atualizando a data inicial para a próxima iteração
                startDate = new Timestamp(endDate.getTime() + (1 * 24 * 60 * 60 * 1000));
            }

            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }
    /**
     * Endpoint responsável por atualizar um projeto.
     *
     * @param data estrutura de dados contendo as informações que se deseja atulizar o projeto
     * @return ResponseEntity confirmando a transação e retornando o id do projeto atualizado.
     * @author Marcus Loureiro
     * @see ProjectRequestDTO
     * @see ResponseEntity
     * @since 08/04/2024
     * */

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> updateProject(@PathVariable String id, @RequestBody ProjectRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Project project = projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            if(data.name() != null){
                project.setName(data.name());
            }
            if(data.deadline() != null){
                project.setDeadline(data.deadline());
            }
            if(data.iterationTime() != null){
                project.setIterationTime(data.iterationTime());
            }
            if(data.workHours() != null){
                project.setWorkHours(data.workHours());
            }
            if(data.clientName() != null){
                project.setClientName(data.clientName());
            }
            project.setModificationDate(new Timestamp(new Date().getTime()));
            projectRepository.save(project);
            response.put("data_id:", project.getId());
            response.put("message", ResponseType.SUCCESS_UPDATE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint responsável por retornar uma lista com todos os projetos cadastrados.
     *
     * @return Lista contendo os projetos no formato ProjectResponseDTO.
     * @throws ResourceNotFoundException Exceção lançada caso não haja projetos cadastrados.
     * @author Marcus Loureiro
     * @see ProjectResponseDTO
     * @since 19/03/2024
     */
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProjectResponseDTO> getAll() throws ResourceNotFoundException {
        List<Project> projects = projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage());
        }
        return projects.stream().map(ProjectResponseDTO::new).toList();
    }

    /**
     * Endpoint responsável por retornar um projeto específico, buscando pelo seu identificador.
     *
     * @param id Identificador único do projeto.
     * @return Projeto no formato ProjectResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso o projeto não seja encontrado.
     * @author Marcus Loureiro
     * @see ProjectResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ProjectResponseDTO getProjectById(@PathVariable String id) throws ResourceNotFoundException {
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        // Atualiza lastAccessDate para a data e hora atual
        project.setLastAccessDate(new Timestamp(new Date().getTime()));
        // Cria as iterações caso não exista nehuma iteração para esse id do projeto
        if(iterationRepository.findByProject(id).isEmpty()){
            createIterations(id);
        }
        projectRepository.save(project);
        return new ProjectResponseDTO(project);
    }

    /**
     * Endpoint responsável por retornar uma lista de projetos criados por um determinado usuário.
     *
     * @param id Identificador único do usuário.
     * @return Lista contendo os projetos no formato ProjectResponseDTO criados pelo usuário informado.
     * @throws ResourceNotFoundException Exceção lançada caso o usuário não possua projetos criados.
     * @author Marcus Loureiro
     * @see ProjectResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/user/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProjectResponseDTO> getProjectByCreatedBy(@PathVariable String id) throws ResourceNotFoundException {
        List<ProjectResponseDTO> projects = projectRepository.findByCreatedBy(id);
        return projects.stream().toList();
    }

    /**
     * Endpoint responsável por retornar uma lista de roles associadas a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo os roles no formato RoleResponseDTO associados ao projeto.
     * @throws ResourceNotFoundException: Exceção lançada caso o projeto não seja encontrado.
     * @author Marcus Loureiro
     * @see RoleResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/{id}/roles")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RoleResponseDTO> getProjectRoles(@PathVariable String id)  {
        return roleRepository.findByProject(id).stream().toList();
    }

    /**
     * Endpoint responsável por retornar uma lista de membros da equipe associados a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo os membros da equipe no formato TeamMemberResponseDTO associados ao projeto.
     * @author Marcus Loureiro
     * @see TeamMemberResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/{id}/teamMembers")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TeamMemberResponseDTO> getTProjectTeamMembers(@PathVariable String id){
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

    /**
     * Endpoint responsável por retornar uma lista de riscos associados a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo os riscos no formato RiskResponseDTO associados ao projeto.
     * @author Marcus Loureiro
     * @see TeamMemberResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/{id}/risks")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RiskResponseDTO> getProjectRisks(@PathVariable String id){
        return riskRepository.findByProject(id).stream().toList();

    }

    /**
     * Endpoint responsável por retornar uma lista de requisitos associados a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo os requisitos no formato RequirementResponseDTO associados ao projeto.
     * @author Marcus Loureiro
     * @see  RequirementResponseDTO
     * @since 19/03/2024
     */

    @GetMapping("/{id}/requirements")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequirementResponseDTO> getRequirementProject(@PathVariable String id){
        return requirementRepository.findByProject(id).stream().toList();
    }


    /**
     * Endpoint responsável por retornar uma lista de requisitos não funcionais associados a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo os requisitos não funcionais no formato NonFunctionalRequirementProjectResponseDTO associados ao projeto.
     * @author Marcus Loureiro
     * @see  NonFunctionalRequirementProjectResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("/{id}/nonFunctionalRequirementsProject")
    @ResponseStatus(code = HttpStatus.OK)
    public List<NonFunctionalRequirementProjectResponseDTO> getNonFunctionalRequirementProject(@PathVariable String id){
        return nonFunctionalRequirementProjectRepository.findByProject(id).stream().toList();
    }

    /**
     * Endpoint responsável por retornar uma lista de tarefas associados a um projeto específico.
     *
     * @param id Identificador único do projeto.
     * @return Lista contendo as tarefas no formato NonFunctionalRequirementProjectResponseDTO associados ao projeto.
     * @author Marcus Loureiro
     * @see  TaskResponseDTO
     * @since 19/03/2024
     */
    @GetMapping("iteration/{id}/tasks")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TaskResponseDTO> getTaskProject(@PathVariable String id){
        return taskRepository.findByIteration(id).stream().toList();
    }





    /**
     * Endpoint responsável por retornar o projeto com todos os seus dados associados dado o id do projeto.
     *
     * @param id Identificador único do projeto.
     * @return ProjectWithJoinResponseDTO
     * @author Marcus Loureiro
     * @see  ProjectWithJoinResponseDTO
     * @since 08/04/2024
     */

    @GetMapping("/{id}/withJoins")
    @ResponseStatus(code = HttpStatus.OK)
    public ProjectWithJoinResponseDTO getProjectWithJoin(@PathVariable String id) throws ResourceNotFoundException{
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        List<RoleResponseDTO> roles = roleRepository.findByProject(id);
        List<TeamMember> teamMembers = teamMemberRepository.findAllByProjectId(id);
        List<RiskResponseDTO> risks = riskRepository.findByProject(id);
        List<RequirementResponseDTO> requirements = requirementRepository.findByProject(id);
        List<NonFunctionalRequirementProjectResponseDTO> nonFunctionalRequirementProject = nonFunctionalRequirementProjectRepository.findByProject(id);
        return new ProjectWithJoinResponseDTO(new ProjectResponseDTO(project), roles, teamMembers, risks, requirements, nonFunctionalRequirementProject);
    }

    /**
     * Endpoint responsável por retornar a lista de projetos usuário pelo seu id.
     *
     * @param id Identificador único do usuário.
     * @return List<ProjectResponseDTO>
     * @author Marcus Loureiro
     * @see  ProjectResponseDTO
     * @since 08/04/2024
     */
    @GetMapping("/recent/user/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProjectResponseDTO> getRecentProjects(@PathVariable String id){
        List<ProjectResponseDTO> projects = projectRepository.findRecentProjects(id);
        return projects.stream().toList();

    }
    /**
     * Endpoint responsável por retornar o backlog do projeto pelo seu id.
     *
     * @param id Identificador único do projeto.
     * @return List<BacklogResponseDTO>
     * @author Marcus Loureiro
     * @see  BacklogResponseDTO
     * @since 15/05/2024
     */
    @GetMapping("/{id}/backlog")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BacklogResponseDTO> getProjectBacklog(@PathVariable String id){
        List<RequirementResponseDTO> listaRequisitos = requirementRepository.findByProject(id);
        List<BacklogResponseDTO>  listaBacklog = new ArrayList<BacklogResponseDTO>();
        int seq = 0;
        for (RequirementResponseDTO requisito : listaRequisitos) {
            System.out.println("requisisto" + requisito.title());
            Requirement r = requirementRepository.findById(requisito.id()).orElseThrow();
            BacklogResponseDTO data = new BacklogResponseDTO(seq, r.getId(), r.getTitle(), r.getPriority(), r.getProgressiveBar());
            listaBacklog.add(data);
            seq++;
        }
        return listaBacklog.stream().toList();

    }

    /**
     * Endpoint responsável por retornar lista de Iterações ativas do projeto pelo seu id se o status for passado.
     *
     * @param id Identificador único do projeto.
     * @return List<IterationResponseDTO>
     * @author Marcus Loureiro
     * @see  IterationResponseDTO
     * @since 15/05/2024
     */
    @GetMapping("/{id}/iteration")
    @ResponseStatus(code = HttpStatus.OK)
    public List<IterationResponseDTO> getProjectIterationStatus(
            @PathVariable String id,
            @RequestParam(name = "active", required = false) Boolean active) {

        List<IterationResponseDTO> listaIteracoes;

        if (active != null) {
            listaIteracoes = iterationRepository.findByProjectAndActive(active, id);
        } else {
            listaIteracoes = iterationRepository.findByProject(id);
        }

        if (Boolean.TRUE.equals(active)) {
            Timestamp currentTimestamp = Timestamp.from(Instant.now());
            listaIteracoes = listaIteracoes.stream()
                    .sorted(Comparator.comparing((IterationResponseDTO iter) ->
                                    !(iter.startDate().compareTo(currentTimestamp) <= 0 && iter.endDate().compareTo(currentTimestamp) >= 0))
                            .thenComparing(IterationResponseDTO::startDate)
                    )
                    .collect(Collectors.toList());
        }

        return listaIteracoes;
    }

    @GetMapping("/{id}/search/requirement")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequirementResponseDTO> getProjectRequirementName(
            @PathVariable String id,
            @RequestParam(name = "requirementName", required = true) String requirementName) {

        List<RequirementResponseDTO> listaRequisitos = requirementRepository.findByNameContaining(requirementName);

        return listaRequisitos.stream().toList();
    }



    /**
     * Endpoint responsável por deletar projetos e consequentemente todos os dados associados ao seu id.
     *
     * @param id Identificador único do projeto.
     * @return HTTPStatus.ok
     * @author Marcus Loureiro
     * @see  Project
     * @since 08/04/2024
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        try {
            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));

            projectRepository.delete(project);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


