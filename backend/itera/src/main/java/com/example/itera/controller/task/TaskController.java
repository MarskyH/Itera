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


    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTask(@RequestBody TaskCompleteRequestDTO data) {
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
                    (TaskRequirement) null,
                    (TaskImprovement) null,
                    (TaskBug) null,
                    iterationData
            );
            taskRepository.save(taskData);

            if(data.taskRequirement() != null && data.taskImprovement() == null && data.taskBug() == null){
                TaskRequirement taskRequirementData = new TaskRequirement(taskData);
                taskData.setTaskRequirement(taskRequirementData);
                taskRequirementRepository.save(taskRequirementData);
                response.put("Task_requirement_id", taskRequirementData.getId());
                taskRepository.save(taskData);
            }else if(data.taskRequirement() == null && data.taskImprovement() != null && data.taskBug() == null){
                TaskImprovement taskImprovementData = new TaskImprovement(taskData);
                taskData.setTaskImprovement(taskImprovementData);
                taskImprovementRepository.save(taskImprovementData);
                taskRepository.save(taskData);
                response.put("Task_improvement_id", taskImprovementData.getId());
            }else if(data.taskRequirement() == null && data.taskImprovement() == null && data.taskBug() != null){
                TaskBug taskBugData = new TaskBug(taskData);
                taskData.setTaskBug(taskBugData);
                taskBugRepository.save(taskBugData);
                taskRepository.save(taskData);
                response.put("Task_bug_id", taskBugData.getId());
            }

            if(data.assigneies() != null){
                for (AssigneeRequestDTO assignee : data.assigneies()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), assignee.deadline(), memberData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

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
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskRequirement().id()).orElse(null);
                contCheck = taskRequirementRepository.getTotalChecksTrueById(taskRequirement.getId());
                if (taskRequirement != null) {
                    // Atualizar os campos do taskRequirement se existir
                    if (data.taskRequirement().checkProject() != null) {
                        if(data.taskRequirement().checkProject()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskRequirement.setCheckProject(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskRequirement.setCheckProject(false);
                        }
                    }
                    if (data.taskRequirement().checkRequirement() != null) {
                        if(data.taskRequirement().checkRequirement()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskRequirement.setCheckRequirement(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskRequirement.setCheckRequirement(false);
                        }
                    }
                    if (data.taskRequirement().checkFront() != null) {
                        if(data.taskRequirement().checkFront()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskRequirement.setCheckFront(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskRequirement.setCheckFront(false);
                        }
                    }
                    if (data.taskRequirement().checkBack() != null) {
                        if(data.taskRequirement().checkBack()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskRequirement.setCheckBack(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskRequirement.setCheckBack(false);
                        }
                    }
                    if (data.taskRequirement().checkTest() != null) {
                        if(data.taskRequirement().checkTest()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskRequirement.setCheckTest(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskRequirement.setCheckTest(false);
                        }
                    }
                    // Adicione outras atualizações de campos de taskRequirement conforme necessário
                    task.setTaskRequirement(taskRequirement);
                    taskRequirementRepository.save(taskRequirement);
                }
            }
            if (data.taskImprovement() != null) {
                TaskImprovement taskImprovement = taskImprovementRepository.findById(data.taskImprovement().id()).orElse(null);
                contCheck = taskImprovementRepository.getTotalChecksTrueById(taskImprovement.getId());
                if (taskImprovement != null) {
                    if (data.taskImprovement().checkProject() != null) {
                        if(data.taskImprovement().checkProject()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskImprovement.setCheckProject(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskImprovement.setCheckProject(false);
                        }
                    }
                    if (data.taskImprovement().checkRequirement() != null) {
                        if(data.taskImprovement().checkRequirement()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskImprovement.setCheckRequirement(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskImprovement.setCheckRequirement(false);
                        }
                    }
                    if (data.taskImprovement().checkFront() != null) {
                        if(data.taskImprovement().checkFront()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskImprovement.setCheckFront(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskImprovement.setCheckFront(false);
                        }
                    }
                    if (data.taskImprovement().checkBack() != null) {
                        if(data.taskImprovement().checkBack()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskImprovement.setCheckBack(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskImprovement.setCheckBack(false);
                        }
                    }
                    if (data.taskImprovement().checkTest() != null) {
                        if(data.taskImprovement().checkTest()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskImprovement.setCheckTest(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskImprovement.setCheckTest(false);
                        }
                    }
                    // Adicione outras atualizações de campos de taskImprovement conforme necessário
                    task.setTaskImprovement(taskImprovement);
                    taskImprovementRepository.save(taskImprovement);
                }
            }
            if (data.taskBug() != null) {
                TaskBug taskBug = taskBugRepository.findById(data.taskBug().id()).orElse(null);
                contCheck = taskBugRepository.getTotalChecksTrueById(taskBug.getId());
                if (taskBug != null) {
                    if (data.taskBug().checkFront() != null) {
                        if(data.taskBug().checkFront()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskBug.setCheckFront(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskBug.setCheckFront(false);
                        }
                    }
                    if (data.taskBug().checkBack() != null) {
                        if(data.taskBug().checkBack()){
                            updateProgressiveBar(contAssignee, contCheck+1);
                            taskBug.setCheckBack(true);
                        }else{
                            updateProgressiveBar(contAssignee, contCheck-1);
                            taskBug.setCheckBack(false);
                        }
                    }
                    if (data.taskBug().checkTest() != null) {
                        if (data.taskBug().checkTest()) {
                            updateProgressiveBar(contAssignee, contCheck + 1);
                            taskBug.setCheckTest(true);
                        } else {
                            updateProgressiveBar(contAssignee, contCheck - 1);
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

            if (data.assigneies() != null){
                for (AssigneeResponseDTO assignee : data.assigneies()) {
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
        return contAssignee/contCheck * 100;
    }
}


