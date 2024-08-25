package com.example.itera.controller.task;


import com.example.itera.controller.requirement.RequirementController;
import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.requirement.Requirement;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.teamMember.TeamMember;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.assignee.AssigneeResponseDTO;
import com.example.itera.dto.pendency.PendencyResponseDTO;
import com.example.itera.dto.requirement.RequirementRequestDTO;
import com.example.itera.dto.requirement.RequirementResponseDTO;
import com.example.itera.dto.task.*;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.pendency.PendencyRepository;
import com.example.itera.repository.requirement.RequirementRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.taskBug.TaskBugRepository;
import com.example.itera.repository.taskImprovement.TaskImprovementRepository;
import com.example.itera.repository.taskRequirement.TaskRequirementRepository;
import com.example.itera.repository.teamMember.TeamMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    TeamMemberRepository teamMemberRepository;

    @Autowired
    RequirementRepository requirementRepository;

    @Autowired
    RequirementController requirementController;


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTask(@RequestBody TaskCompleteRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        if (data.orderTask() == null) {
            order = taskRepository.findByIteration(data.iteration_id()).size() + 1;
        } else {
            order = data.orderTask();
        }
        if (data.listName() != null) {
            listName = data.listName();
        }
        Task taskData = null;
        String requirementId = null;
        try {
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
                    (TaskRequirement) null,
                    (TaskImprovement) null,
                    (TaskBug) null,
                    iterationData
            );
            taskRepository.save(taskData);
            try {
                if (data.taskRequirement() != null && data.taskImprovement() == null && data.taskBug() == null) {
                    TaskRequirement taskRequirementData = new TaskRequirement(taskData);
                    taskData.setTaskRequirement(taskRequirementData);
                    taskRequirementRepository.save(taskRequirementData);
                    response.put("Task_requirement_id", taskRequirementData.getId());
                    taskRepository.save(taskData);
                } else if (data.taskRequirement() == null && data.taskImprovement() != null && data.taskBug() == null) {
                    TaskImprovement taskImprovementData = new TaskImprovement(taskData);
                    requirementController.saveRequirement(new RequirementRequestDTO(taskData.getTitle(), taskData.getDetails(), taskData.getComplexity(), taskData.getPriority(), Integer.valueOf(taskData.getEffort()), taskData.getSizeTask(), null, iterationData.getId(), iterationData.getProject().getId()));
                    if(data.taskImprovement().requirement_id() !=  null){
                        taskImprovementData.setRequirementId(data.taskImprovement().requirement_id());
                    }
                    requirementId = requirementRepository.findByName(taskData.getTitle()).getId();
                    taskImprovementData.setRequirementId(requirementId);
                    taskData.setTaskImprovement(taskImprovementData);
                    taskImprovementRepository.save(taskImprovementData);
                    taskRepository.save(taskData);
                    response.put("Task_improvement_id", taskImprovementData.getId());
                } else if (data.taskRequirement() == null && data.taskImprovement() == null && data.taskBug() != null) {
                    TaskBug taskBugData = new TaskBug(taskData);
                    if(data.taskBug().requirement_id() !=  null){
                        taskBugData.setRequirementId(data.taskBug().requirement_id());
                    }
                    taskData.setTaskBug(taskBugData);
                    taskBugRepository.save(taskBugData);
                    taskRepository.save(taskData);
                    response.put("Task_bug_id", taskBugData.getId());
                }
            } catch (Exception e) {
                taskRepository.deleteById(taskData.getId());
                if(requirementId != null && !requirementId.isEmpty()){
                    requirementRepository.deleteById(requirementId);
                }
                return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage() + " DETAILS: " + e);
            }


            if (data.assignees() != null) {
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), assignee.deadline(), memberData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

            response.put("task_id", taskData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            if(taskData != null) {
                taskRepository.deleteById(taskData.getId());
            }
            if(requirementId != null && !requirementId.isEmpty()){
                requirementRepository.deleteById(requirementId);
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            if(taskData != null) {
                taskRepository.deleteById(taskData.getId());
            }
            if(requirementId != null && !requirementId.isEmpty()){
                requirementRepository.deleteById(requirementId);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            if(taskData != null) {
                taskRepository.deleteById(taskData.getId());
            }
            if(requirementId != null && !requirementId.isEmpty()){
                requirementRepository.deleteById(requirementId);
            }
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    /*@PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTask(@RequestBody TaskRequestRequirementDTO data) {
        Map<String, String> response = new HashMap<>();
        if(data.orderTask() == null){
            order = taskRepository.findByIteration(data.iteration_id()).size()+1;
        }else{
            order = data.orderTask();
        }
        if(data.listName() != null){
            listName = data.listName();
        }
        try {
            Iteration iterationData = iterationRepository.findById(data.iteration_id()).orElseThrow();
            TaskRequirement taskRequirementData = new TaskRequirement();
            Task taskData = new Task(
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
                    taskRequirementData,
                    iterationData
            );
            taskRepository.save(taskData);
            response.put("task_id:", taskData.getId());
            response.put("message:", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }*/


    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TaskCompleteResponseDTO getTaskById(@PathVariable String id) throws ResourceNotFoundException {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        List<AssigneeResponseDTO> assigneeResponseDTOList = assigneeRepository.findByTask(task.getId());
        List<PendencyResponseDTO> pendencyResponseDTOList = pendencyRepository.findByTask(task.getId());
        return new TaskCompleteResponseDTO(task, assigneeResponseDTOList, pendencyResponseDTOList);
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
            }else if (data.listName() != null){
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
                contCheck = taskRequirementRepository.getTotalChecksTrueById(taskRequirement.getId());
                System.out.println("CONTCHEK:" + contCheck);
                if (taskRequirement != null) {
                    Requirement requirement = requirementRepository.findByName(task.getTitle());
                    // Atualizar os campos do taskRequirement se existir
                    if (data.taskRequirement().checkProject() != null) {
                        if(data.taskRequirement().checkProject()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskRequirement.setCheckProject(true);
                        }else{
                            taskRequirement.setCheckProject(false);
                        }
                    }
                    if (data.taskRequirement().checkRequirement() != null) {
                        if(data.taskRequirement().checkRequirement()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskRequirement.setCheckRequirement(true);
                        }else{
                            taskRequirement.setCheckRequirement(false);
                        }
                    }
                    if (data.taskRequirement().checkFront() != null) {
                        if(data.taskRequirement().checkFront()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskRequirement.setCheckFront(true);
                        }else{
                            taskRequirement.setCheckFront(false);
                        }
                    }
                    if (data.taskRequirement().checkBack() != null) {
                        if(data.taskRequirement().checkBack()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskRequirement.setCheckBack(true);
                        }else{
                            taskRequirement.setCheckBack(false);
                        }
                    }
                    if (data.taskRequirement().checkTest() != null) {
                        if(data.taskRequirement().checkTest()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskRequirement.setCheckTest(true);
                        }else{
                            taskRequirement.setCheckTest(false);
                        }
                    }
                    // Adicione outras atualizações de campos de taskRequirement conforme necessário
                    task.setTaskRequirement(taskRequirement);
                    taskRequirementRepository.save(taskRequirement);
                }
            }
            if (data.taskImprovement() != null) {
                TaskImprovement taskImprovement = taskImprovementRepository.findById(task.getTaskImprovement().getId()).orElse(null);
                contCheck = taskImprovementRepository.getTotalChecksTrueById(taskImprovement.getId());
                Requirement requirement = requirementRepository.findByName(task.getTitle());
                if (taskImprovement != null) {
                    if (data.taskImprovement().checkProject() != null) {
                        if(data.taskImprovement().checkProject()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskImprovement.setCheckProject(true);
                        }else{
                            taskImprovement.setCheckProject(false);
                        }
                    }
                    if (data.taskImprovement().checkRequirement() != null) {
                        if(data.taskImprovement().checkRequirement()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskImprovement.setCheckRequirement(true);
                        }else{
                            taskImprovement.setCheckRequirement(false);
                        }
                    }
                    if (data.taskImprovement().checkFront() != null) {
                        if(data.taskImprovement().checkFront()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskImprovement.setCheckFront(true);
                        }else{
                            taskImprovement.setCheckFront(false);
                        }
                    }
                    if (data.taskImprovement().checkBack() != null) {
                        if(data.taskImprovement().checkBack()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskImprovement.setCheckBack(true);
                        }else{
                            taskImprovement.setCheckBack(false);
                        }
                    }
                    if (data.taskImprovement().checkTest() != null) {
                        if(data.taskImprovement().checkTest()){
                            int progressiveBar = updateProgressiveBar(contAssignee, contCheck+1);
                            task.setProgressiveBar(progressiveBar);
                            requirement.setProgressiveBar(progressiveBar);
                            taskImprovement.setCheckTest(true);
                        }else{
                            taskImprovement.setCheckTest(false);
                        }
                    }
                    // Adicione outras atualizações de campos de taskImprovement conforme necessário
                    task.setTaskImprovement(taskImprovement);
                    taskImprovementRepository.save(taskImprovement);
                }
            }
            if (data.taskBug() != null) {
                TaskBug taskBug = taskBugRepository.findById(task.getTaskBug().getId()).orElse(null);
                contCheck = taskBugRepository.getTotalChecksTrueById(taskBug.getId());
                if (taskBug != null) {
                    if (data.taskBug().checkFront() != null) {
                        if(data.taskBug().checkFront()){
                            task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck+1));
                            taskBug.setCheckFront(true);
                        }else{
                            task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));

                            taskBug.setCheckFront(false);
                        }
                    }
                    if (data.taskBug().checkBack() != null) {
                        if(data.taskBug().checkBack()){
                            task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck+1));
                            taskBug.setCheckBack(true);
                        }else{
                            task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck));

                            taskBug.setCheckBack(false);
                        }
                    }
                    if (data.taskBug().checkTest() != null) {
                        if (data.taskBug().checkTest()) {
                            task.setProgressiveBar(updateProgressiveBar(contAssignee, contCheck+1));
                            taskBug.setCheckTest(true);
                        } else {
                            taskBug.setCheckTest(false);
                        }
                    }
                    // Adicione outras atualizações de campos de taskBug conforme necessário
                    task.setTaskBug(taskBug);
                    taskBugRepository.save(taskBug);
                }
            }
            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }

            if (data.assignees() != null){
                for (AssigneeResponseDTO assignee : data.assignees()) {
                    Assignee assigneeData;
                        try{
                            assigneeData = assigneeRepository.findByMemberIdStep(assignee.taskStep(), assignee.member_id());
                            if(assigneeData == null){
                                assigneeData = new Assignee();
                            }
                            TeamMember member = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                            assigneeData.setDeadline(assignee.deadline());
                            assigneeData.setTaskStep(assignee.taskStep());
                            assigneeData.setTeamMember(member);
                            assigneeData.setTask(task);
                            assigneeRepository.save(assigneeData);
                        }catch (Exception e){
                            return ResponseEntity.notFound().build();
                        }
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
            if(dataTask.getTaskRequirement()!= null){
                taskRequirement = dataTask.getTaskRequirement();
            }
            if(dataTask.getTaskImprovement()!= null){
                taskImprovement = dataTask.getTaskImprovement();
            }
            if(dataTask.getTaskBug()!= null){
                taskBug = dataTask.getTaskBug();
            }
            TaskListResponseDTO item = new TaskListResponseDTO(dataTask, taskRequirement, taskImprovement, taskBug);
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
        System.out.println("FUNCAO PROGRESSIVE BAR:" + contAssignee + "/" + contCheck);
        double percentage = ((double) contCheck / contAssignee) * 100;
        System.out.println("FUNCAO PROGRESSIVE BAR:" + percentage);
        return (int) percentage;
    }

}


