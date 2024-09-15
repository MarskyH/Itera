package com.example.itera.controller.task;


import com.example.itera.controller.requirement.RequirementController;
import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.project.Project;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.role.Role;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskPlanning.TaskPlanning;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.taskReview.TaskReview;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.domain.user.User;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.dto.project.ProjectResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.role.RoleResponseDTO;
import com.example.itera.dto.task.*;
import com.example.itera.dto.teamMember.TeamMemberPlanningResponseDTO;
import com.example.itera.dto.teamMember.TeamMemberResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.repository.TaskPlanning.TaskPlanningRepository;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.pendency.PendencyRepository;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.role.RoleRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.taskBug.TaskBugRepository;
import com.example.itera.repository.taskImprovement.TaskImprovementRepository;
import com.example.itera.repository.taskRequirement.TaskRequirementRepository;
import com.example.itera.repository.taskReview.TaskReviewRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Responsável por fornecer endpoints para manipulação de tarefas
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 05/05/2024
 */

@RestController
@RequestMapping("task")
public class TaskController {

    int order = 0;
    String listName = "A fazer";

    @Autowired
    IterationRepository iterationRepository;

    @Autowired
    PendencyRepository pendencyRepository;

    @Autowired
    AssigneeRepository assigneeRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskRequirementRepository taskRequirementRepository;
    @Autowired
    TaskImprovementRepository taskImprovementRepository;

    @Autowired
    TaskBugRepository taskBugRepository;

    @Autowired
    TaskPlanningRepository taskPlanningRepository;

    @Autowired
    TaskReviewRepository taskReviewRepository;
    @Autowired
    TeamMemberRepository teamMemberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    RequirementController requirementController;


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTask(@RequestBody TaskCompleteRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        String listName = data.listName() != null ? data.listName() : "A fazer";
        Task taskData = null;
        String requirementId = null;

        try {
            int order = data.orderTask() != null
                    ? data.orderTask()
                    : taskRepository.findByIteration(data.iteration_id()).size() + 1;

            Iteration iterationData = iterationRepository.findById(data.iteration_id()).orElseThrow();

            taskData = new Task(
                    data.title(),
                    data.priority(),
                    data.details(),
                    data.complexity(),
                    data.effort(),
                    data.sizeTask(),
                    data.startDate(),
                    data.endDate(),
                    order,
                    listName,
                    data.taskType(),
                    null,  // TaskRequirement
                    null,  // TaskImprovement
                    null,  // TaskBug
                    null, // taskPlanning
                    null, // tasReview
                    iterationData
            );

            // Salvando a task
            taskRepository.save(taskData);

            // Tratamento de TaskRequirement
            if (data.taskRequirement() != null && data.taskImprovement() == null && data.taskBug() == null && data.taskPlanning() == null) {
                TaskRequirement taskRequirementData = new TaskRequirement(taskData);
                taskData.setTaskRequirement(taskRequirementData);
                taskRequirementRepository.save(taskRequirementData);
                taskRepository.save(taskData);
                response.put("Task_requirement_id", taskRequirementData.getId());
            }

            // Tratamento de TaskImprovement
            else if (data.taskRequirement() == null && data.taskImprovement() != null && data.taskBug() == null && data.taskPlanning() == null) {
                TaskImprovement taskImprovementData = new TaskImprovement(taskData);

                // Lógica para salvar Requirement, se necessário
                if (data.taskImprovement().requirement_id() == null) {
                    requirementController.saveRequirement(
                            new RequirementRequestDTO(
                                    taskData.getTitle(),
                                    taskData.getDetails(),
                                    taskData.getComplexity(),
                                    taskData.getPriority(),
                                    Integer.valueOf(taskData.getEffort()),
                                    taskData.getSizeTask(),
                                    null,
                                    iterationData.getId(),
                                    iterationData.getProject().getId()
                            )
                    );
                }

                requirementId = requirementRepository.findByName(taskData.getTitle()).getId();
                taskImprovementData.setRequirementId(requirementId);
                taskData.setTaskImprovement(taskImprovementData);
                taskImprovementRepository.save(taskImprovementData);
                taskRepository.save(taskData);
                response.put("Task_improvement_id", taskImprovementData.getId());
            }

            // Tratamento de TaskBug
            else if (data.taskRequirement() == null && data.taskImprovement() == null && data.taskBug() != null && data.taskPlanning() == null) {
                TaskBug taskBugData = new TaskBug(taskData);

                if (data.taskBug().requirement_id() != null) {
                    taskBugData.setRequirementId(data.taskBug().requirement_id());
                }

                taskData.setTaskBug(taskBugData);
                taskBugRepository.save(taskBugData);
                taskRepository.save(taskData);
                response.put("Task_bug_id", taskBugData.getId());
            }

            // Tratamento de TaskPlanning
            else if (data.taskRequirement() == null && data.taskImprovement() == null && data.taskBug() == null && data.taskPlanning() != null) {
                TaskPlanning taskPlanningData = new TaskPlanning(
                        taskData,
                        data.taskPlanning().totalSize(),
                        data.taskPlanning().totalEffort(),
                        data.taskPlanning().plannedSpeed(),
                        data.taskPlanning().projectBacklog(),
                        completeTeamMembers(data.taskPlanning().projectMembers())
                );
                taskData.setTaskPlanning(taskPlanningData);
                taskPlanningRepository.save(taskPlanningData);
                taskRepository.save(taskData);
                response.put("Task_planning_id", taskPlanningData.getId());
            }

            else if (data.taskRequirement() == null && data.taskImprovement() == null && data.taskBug() == null && data.taskPlanning() == null && data.taskReview() != null) {
                TaskReview taskReviewData = new TaskReview(
                        taskData,
                        data.taskReview().totalSize(),
                        data.taskReview().totalEffort(),
                        data.taskReview().completedSpeed(),
                        data.taskReview().iterationBacklog(),
                        data.taskReview().completedScope(),
                        data.taskReview().participatingMembers(),
                        data.taskReview().checkHumanResources(),
                        data.taskReview().checkScope(),
                        data.taskReview().checkSpeed(),
                        data.taskReview().checkRisks()
                );
                taskData.setTaskReview(taskReviewData);
                taskReviewRepository.save(taskReviewData);
                taskRepository.save(taskData);
                response.put("Task_review_id", taskReviewData.getId());
            }

            // Adicionando Assignees, se houver
            if (data.assignees() != null) {
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(
                            assignee.taskStep(),
                            assignee.deadline(),
                            memberData,
                            taskData
                    );
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

            // Retorna a resposta de sucesso
            response.put("task_id", taskData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            handleTaskError(taskData, requirementId);
            return ResponseEntity.internalServerError().body("Error saving task: " + e.getMessage());
        }
    }

    // Método auxiliar para tratar rollback
    private void handleTaskError(Task taskData, String requirementId) {
        if (taskData != null) {
            taskRepository.deleteById(taskData.getId());
        }
        if (requirementId != null && !requirementId.isBlank()) {
            requirementRepository.deleteById(requirementId);
        }
    }


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TaskCompleteResponseDTO getTaskById(@PathVariable String id) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        List<AssigneeResponseDTO> assigneeResponseDTOList = assigneeRepository.findByTask(task.getId());
        List<PendencyResponseDTO> pendencyResponseDTOList = pendencyRepository.findByTask(task.getId());
        return new TaskCompleteResponseDTO(task, assigneeResponseDTOList, pendencyResponseDTOList);
    }

    @GetMapping("/{id}/pendencies")
    @ResponseStatus(code = HttpStatus.OK)
    public List<PendencyResponseDTO> getPendenciesByTaskId(@PathVariable String id) throws ResourceNotFoundException {
        List<PendencyResponseDTO> pendencyResponseDTOList = null;
        try {
            pendencyResponseDTOList = pendencyRepository.findByTask(id);
        } catch (Exception e) {
            pendencyResponseDTOList = new ArrayList<>();
        }
        return pendencyResponseDTOList;
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaskById(@PathVariable String id, @RequestBody TaskCompleteResponseDTO data) {
        Map<String, String> response = new HashMap<>();

        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
            int contAssignee = assigneeRepository.findByTask(task.getId()).size();
            int contCheck = 0;
            boolean isListNameValided = task.updateTaskListName(task.getListName(), data.listName());
            if (data.listName() != null && isListNameValided) {
                task.setListName(data.listName());
            } else if (data.listName() != null) {
                response.put("data_id", task.getId());
                response.put("message", ResponseType.FAIL_UPDATE.getMessage() + " - " + data.listName() + " to " + task.getListName());
                return ResponseEntity.badRequest().body(response);
            }

            // Atualizar apenas os campos fornecidos pelo usuário
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.details() != null) {
                task.setDetails(data.details());
            }
            if (data.complexity() != null) {
                task.setComplexity(data.complexity());
            }
            if (data.effort() != null) {
                task.setEffort(data.effort());
            }
            if (data.sizeTask() != null) {
                task.setSizeTask(data.sizeTask());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.orderTask() != null) {
                task.setOrderTask(data.orderTask());
            }
            if (data.checkCancelled() != null) {
                task.setCheckCancelled(data.checkCancelled());
            }
            if (data.detailsCancelled() != null) {
                task.setDetailsCancelled(data.detailsCancelled());
            }
            if (data.taskType() != null) {
                task.setTaskType(data.taskType());
            }

            if (data.taskRequirement() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(task.getTaskRequirement().getId()).orElse(null);
                if (taskRequirement != null) {
                    if (data.taskRequirement().checkProject() != null) {
                        taskRequirement.setCheckProject(data.taskRequirement().checkProject());
                    }
                    if (data.taskRequirement().checkRequirement() != null) {
                        taskRequirement.setCheckRequirement(data.taskRequirement().checkRequirement());
                    }
                    if (data.taskRequirement().checkFront() != null) {
                        taskRequirement.setCheckFront(data.taskRequirement().checkFront());
                    }
                    if (data.taskRequirement().checkBack() != null) {
                        taskRequirement.setCheckBack(data.taskRequirement().checkBack());
                    }
                    if (data.taskRequirement().checkTest() != null) {
                        taskRequirement.setCheckTest(data.taskRequirement().checkTest());
                    }
                    task.setTaskRequirement(taskRequirement);
                    taskRequirementRepository.save(taskRequirement);
                    contCheck = taskRequirementRepository.getTotalChecksTrueById(taskRequirement.getId());
                    task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));
                    Requirement requirement = requirementRepository.findByName(task.getTitle());
                    requirement.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));
                    if(requirement.getProgressiveBar() == 100){
                        requirement.setDone(true);
                    }
                    requirementRepository.save(requirement);
                    taskRepository.save(task);
                }
            }

            if (data.taskImprovement() != null) {
                TaskImprovement taskImprovement = taskImprovementRepository.findById(task.getTaskImprovement().getId()).orElse(null);
                if (taskImprovement != null) {
                    if (data.taskImprovement().checkProject() != null) {
                        taskImprovement.setCheckProject(data.taskImprovement().checkProject());
                    }
                    if (data.taskImprovement().checkRequirement() != null) {
                        taskImprovement.setCheckRequirement(data.taskImprovement().checkRequirement());
                    }
                    if (data.taskImprovement().checkFront() != null) {
                        taskImprovement.setCheckFront(data.taskImprovement().checkFront());
                    }
                    if (data.taskImprovement().checkBack() != null) {
                        taskImprovement.setCheckBack(data.taskImprovement().checkBack());
                    }
                    if (data.taskImprovement().checkTest() != null) {
                        taskImprovement.setCheckTest(data.taskImprovement().checkTest());
                    }
                    task.setTaskImprovement(taskImprovement);
                    taskImprovementRepository.save(taskImprovement);
                    contCheck = taskImprovementRepository.getTotalChecksTrueById(taskImprovement.getId());
                    task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));
                    Requirement requirement = requirementRepository.findByName(task.getTitle());
                    requirement.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));
                    if(requirement.getProgressiveBar() == 100){
                        requirement.setDone(true);
                    }
                    requirementRepository.save(requirement);
                    taskRepository.save(task);
                }
            }

            if (data.taskBug() != null) {
                TaskBug taskBug = taskBugRepository.findById(task.getTaskBug().getId()).orElse(null);
                if (taskBug != null) {
                    if (data.taskBug().checkFront() != null) {
                        taskBug.setCheckFront(data.taskBug().checkFront());
                    }
                    if (data.taskBug().checkBack() != null) {
                        taskBug.setCheckBack(data.taskBug().checkBack());
                    }
                    if (data.taskBug().checkTest() != null) {
                        taskBug.setCheckTest(data.taskBug().checkTest());
                    }
                    task.setTaskBug(taskBug);
                    taskBugRepository.save(taskBug);
                    contCheck = taskBugRepository.getTotalChecksTrueById(taskBug.getId());
                    task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));
                    taskRepository.save(task);
                }
            }

            if(data.taskPlanning() != null){
                TaskPlanning taskPlanning = taskPlanningRepository.findById(task.getTaskPlanning().getId()).orElse(null);
                if(taskPlanning != null){
                    if(data.taskPlanning().projectBacklog() != null){
                        taskPlanning.setProjectBacklog(taskPlanning.setProjectBacklogAsJson(data.taskPlanning().projectBacklog()));
                    }
                    if(data.taskPlanning().projectMembers() != null){
                        taskPlanning.setProjectMembers(taskPlanning.setProjectMembersAsJson(data.taskPlanning().projectMembers()));
                    }
                    taskPlanning.setTotalSize(taskPlanning.getSizeCalculation(taskPlanning.getProjectBacklogAsList()));
                    taskPlanning.setTotalEffort(taskPlanning.getEffortCalculation(taskPlanning.getProjectBacklogAsList()));

                    if(taskPlanning.getTotalEffort() != 0 && taskPlanning.getTotalSize() != 0){
                        taskPlanning.setPlannedSpeed((double) taskPlanning.getTotalSize() / taskPlanning.getTotalEffort());
                    }else{
                        taskPlanning.setPlannedSpeed(0.0);
                    }
                    task.setTaskPlanning(taskPlanning);
                    taskPlanningRepository.save(taskPlanning);
                }
            }

            if(data.taskReview() != null){
                TaskReview taskReview = taskReviewRepository.findById(task.getTaskReview().getId()).orElse(null);
                if(taskReview != null){

                    if(data.taskReview().participatingMembers() != null){
                        taskReview.setParticipatingMembers(taskReview.setParticipatingMembersAsJson(data.taskReview().participatingMembers()));
                    }
                    if (taskReview.getCheckHumanResources() != null && !taskReview.getCheckHumanResources().equals(data.taskReview().checkHumanResources())) {
                        taskReview.setCheckHumanResources(data.taskReview().checkHumanResources());
                    }

                    if (taskReview.getCheckScope() != null && !taskReview.getCheckScope().equals(data.taskReview().checkScope())) {
                        taskReview.setCheckScope(data.taskReview().checkScope());
                    }

                    if (taskReview.getCheckSpeed() != null && !taskReview.getCheckSpeed().equals(data.taskReview().checkSpeed())) {
                        taskReview.setCheckSpeed(data.taskReview().checkSpeed());
                    }

                    if (taskReview.getCheckRisks() != null && !taskReview.getCheckRisks().equals(data.taskReview().checkRisks())) {
                        taskReview.setCheckRisks(data.taskReview().checkRisks());
                    }

                    if (data.taskReview().iterationBacklog() != null) {
                        taskReview.setIterationBacklog(taskReview.setIterationBacklogAsJson(data.taskReview().iterationBacklog()));
                    }
                    if (data.taskReview().completedScope() != null) {
                        taskReview.setCompletedScope(taskReview.setCompletedScopeAsJson(data.taskReview().completedScope()));
                    }
                    taskReview.setTotalSize(taskReview.getSizeCalculation(taskReview.getCompletedScopelogAsList()));
                    taskReview.setTotalEffort(taskReview.getEffortCalculation(taskReview.getCompletedScopelogAsList()));
                    if (taskReview.getTotalEffort() != 0 && taskReview.getTotalSize() != 0) {
                        taskReview.setCompletedSpeed((double) taskReview.getTotalSize() / taskReview.getTotalEffort());
                    } else {
                        taskReview.setCompletedSpeed(0.0);
                    }

                    task.setTaskReview(taskReview);
                    taskReviewRepository.save(taskReview);
                }
            }
            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }

            if (data.assignees() != null) {
                for (AssigneeResponseDTO assignee : data.assignees()) {
                    Assignee assigneeData;
                    try {
                        assigneeData = assigneeRepository.findByMemberIdStep(assignee.taskStep(), assignee.member_id(), task.getId());
                        if (assigneeData == null) {
                            assigneeData = new Assignee();
                        }
                        TeamMember member = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                        assigneeData.setDeadline(assignee.deadline());
                        assigneeData.setTaskStep(assignee.taskStep());
                        assigneeData.setTeamMember(member);
                        assigneeData.setTask(task);
                        assigneeRepository.save(assigneeData);
                    } catch (Exception e) {
                        return ResponseEntity.notFound().build();
                    }
                }
            }
            if(data.detailsCancelled() != null && !data.detailsCancelled().isBlank()){
                cancelTaskById(task.getId(), new TaskCancelledRequestDTO(data.detailsCancelled()));
            }
            taskRepository.save(task);
            response.put("data_id", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTaskById(@PathVariable String id, @RequestBody TaskCancelledRequestDTO data) {
        Map<String, String> response = new HashMap<>();

        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));

            if(data.detailsCancelled() != null){
                task.setDetailsCancelled(data.detailsCancelled());
                task.setCheckCancelled(true);
                task.setListName("Cancelado");
                task.setProgressiveBar(0);
                try{
                    Requirement requirement = requirementRepository.findByName(task.getTitle());
                    requirement.setProgressiveBar(0);
                    requirement.setIterationId(null);
                    requirement.setDone(false);
                    requirement.setCheckCancelled(true);
                    requirementRepository.save(requirement);
                    response.put("requirement", "Requisisto afetado " + requirement.getId() + "-" + requirement.getTitle());
                }catch (Exception e){
                    response.put("requirement", "Não se trata de tarefa requisito ou melhoria");
                }
            }
            taskRepository.save(task);
            response.put("data_id", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/iteration/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public List<TaskListResponseDTO> getTaskById(@PathVariable String id,
                                                 @RequestParam(name = "listName", required = false) String listName)throws ResourceNotFoundException {
        List<TaskResponseDTO> tasks;
        if(!Objects.equals(listName, "")){
            listName = listName.replace("+", " ");
            tasks = taskRepository.findByIterationWithListName(id, listName);
        }else{
            tasks = taskRepository.findByIteration(id);
        }

        List<TaskListResponseDTO> listaTasks = new ArrayList<>();
        for(TaskResponseDTO task : tasks){
            Task dataTask = taskRepository.findById(task.id()).orElseThrow();

            TaskRequirement taskRequirement = new TaskRequirement();
            TaskImprovement taskImprovement = new TaskImprovement();
            TaskBug taskBug = new TaskBug();
            TaskPlanning taskPlanning = new TaskPlanning();
            if(dataTask.getTaskRequirement()!= null){
                taskRequirement = dataTask.getTaskRequirement();
            }
            if(dataTask.getTaskImprovement()!= null){
                taskImprovement = dataTask.getTaskImprovement();
            }
            if(dataTask.getTaskBug()!= null){
                taskBug = dataTask.getTaskBug();
            }
            if(dataTask.getTaskPlanning() != null){
                taskPlanning = dataTask.getTaskPlanning();
            }
            TaskListResponseDTO item = new TaskListResponseDTO(dataTask, taskRequirement, taskImprovement, taskBug, taskPlanning);
            listaTasks.add(item);
        }
        return listaTasks;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            taskRepository.delete(taskRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public int updateProgressiveBar(int contAssignee, int contCheck){
        if(contAssignee == 0){
            return 0;
        }else{
            double percentage = ((double) contCheck / contAssignee) * 100;
            if(percentage < 100 && contCheck == contAssignee){
                return 100;
            }
            return (int) percentage;
        }
    }

    public  List<TeamMemberPlanningResponseDTO> completeTeamMembers(List<TeamMemberPlanningResponseDTO> members) {
        if(members != null) {
            List<TeamMemberPlanningResponseDTO> completeTeamMembers = new ArrayList<>();

            for (TeamMemberPlanningResponseDTO member : members) {
                TeamMember memberBD = teamMemberRepository.findById(member.id()).orElseThrow();

                // Obtenha o UserResponseDTO correspondente ao usuário
                UserResponseDTO userDTO = userRepository.findById(memberBD.getUser().getId())
                        .map(UserResponseDTO::new) // Converte o User para UserResponseDTO
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // Obtenha o RoleResponseDTO correspondente ao papel
                RoleResponseDTO roleDTO = roleRepository.findById(memberBD.getRole().getId())
                        .map(RoleResponseDTO::new) // Converte o Role para RoleResponseDTO
                        .orElseThrow(() -> new RuntimeException("Role not found"));

                // Obtenha o ProjectResponseDTO correspondente ao projeto
                ProjectResponseDTO projectDTO = projectRepository.findById(memberBD.getProject().getId())
                        .map(ProjectResponseDTO::new) // Converte o Project para ProjectResponseDTO
                        .orElseThrow(() -> new RuntimeException("Project not found"));

                // Crie o TeamMemberResponseDTO com os DTOs
                TeamMemberPlanningResponseDTO dto = new TeamMemberPlanningResponseDTO(
                        member.id(),
                        member.hourlyRate(),
                        member.dedicatedHours(),
                        userDTO,
                        roleDTO,
                        projectDTO
                );

                completeTeamMembers.add(dto);
            }
            return completeTeamMembers;
        }else{
            return new ArrayList<>();
        }
    }




}


