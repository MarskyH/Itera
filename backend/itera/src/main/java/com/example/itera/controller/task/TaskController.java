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
    @Lazy
    RequirementController requirementController;

    @PostMapping("/type/improvement")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTaskImprovement(@RequestBody TaskTaskImprovementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        if( data.task().orderTask() == null){
            order = taskRepository.findByIteration( data.task().iteration_id()).size()+1;
        }else{
            order =  data.task().orderTask();
        }
        try {
            TaskRequestImprovementDTO dataTask = data.task();

            Iteration iterationData = iterationRepository.findById(dataTask.iteration_id()).orElseThrow();
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
                    dataTask.details(),
                    dataTask.complexity(),
                    dataTask.effort(),
                    requirementController.getSizeRequirement(dataTask.sizeTask()),
                    dataTask.startDate(),
                    dataTask.endDate(),
                    order,
                    listName,
                    data.task().taskType(),
                    (TaskImprovement) null,
                    iterationData
            );
            taskRepository.save(taskData); // Salve a Task primeiro

            TaskImprovement taskImprovementData = new TaskImprovement(
                    taskData
            );
            taskImprovementRepository.save(taskImprovementData);

            taskData.setTaskImprovement(taskImprovementData);
            taskRepository.save(taskData);

            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), assignee.deadline(), memberData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

            response.put("task_id", taskData.getId());
            response.put("taskImprovement_id", taskImprovementData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            requirementController.saveRequirement(new RequirementRequestDTO(dataTask.title(), dataTask.details(), dataTask.complexity(), dataTask.priority(), Integer.valueOf(dataTask.effort()), dataTask.sizeTask(), null, iterationData.getId(), iterationData.getProject().getId()));


            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    int order = 0;
    String listName = "A fazer";

    @PostMapping
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
    }

    @PostMapping("/type/requirement")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTaskRequirement(@RequestBody TaskTaskRequirementRequestDTO data) {
        TaskRequestRequirementDTO dataTask = data.task();
        Iteration iteration;
        iteration = iterationRepository.findById(data.task().iteration_id()).orElseThrow(EntityNotFoundException::new);
        Map<String, String> response = new HashMap<>();
        if( data.task().orderTask() == null){
            order = taskRepository.findByIteration( data.task().iteration_id()).size()+1;
        }else{
            order =  data.task().orderTask();
        }
        try {
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
                    dataTask.details(),
                    dataTask.complexity(),
                    dataTask.effort(),
                    dataTask.sizeTask(),
                    dataTask.startDate(),
                    dataTask.endDate(),
                    order,
                    listName,
                    data.task().taskType(),
                    (TaskRequirement) null,
                    iteration
            );
            taskRepository.save(taskData); // Salve a Task primeiro

            TaskRequirement taskRequirementData = new TaskRequirement(
                    taskData
            );
            taskRequirementRepository.save(taskRequirementData);

            taskData.setTaskRequirement(taskRequirementData);
            taskRepository.save(taskData);

            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), assignee.deadline(), memberData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

            response.put("task_id", taskData.getId());
            response.put("taskRequirement_id", taskRequirementData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }


    @PostMapping("/type/bug")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveTaskBug(@RequestBody TaskTaskBugRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        if( data.task().orderTask() == null){
            order = taskRepository.findByIteration( data.task().iteration_id()).size()+1;
        }else{
            order =  data.task().orderTask();
        }
        try {
            TaskRequestBugDTO dataTask = data.task();

            Iteration iterationData = iterationRepository.findById(dataTask.iteration_id()).orElseThrow();
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
                    dataTask.details(),
                    dataTask.complexity(),
                    dataTask.effort(),
                    requirementController.getSizeRequirement(dataTask.sizeTask()),
                    dataTask.startDate(),
                    dataTask.endDate(),
                    order,
                    listName,
                    data.task().taskType(),
                    (TaskBug) null,
                    iterationData
            );
            taskRepository.save(taskData);

            TaskBug taskBugData = new TaskBug(
                    taskData
            );
            taskBugRepository.save(taskBugData);

            taskData.setTaskBug(taskBugData);
            taskRepository.save(taskData);
            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    TeamMember memberData = teamMemberRepository.findById(assignee.member_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), assignee.deadline(), memberData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }


            response.put("task_id", taskData.getId());
            response.put("taskBug_id", taskBugData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
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



    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaskById(@PathVariable String id, @RequestBody TaskCompleteResponseDTO data) {
        Map<String, String> response = new HashMap<>();

        try {
            Task task = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
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
                if (taskRequirement != null) {
                    // Atualizar os campos do taskRequirement se existir
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
                    // Adicione outras atualizações de campos de taskRequirement conforme necessário
                    task.setTaskRequirement(taskRequirement);
                    taskRequirementRepository.save(taskRequirement);
                }
            }
            if (data.taskImprovement() != null) {
                TaskImprovement taskImprovement = taskImprovementRepository.findById(data.taskImprovement().id()).orElse(null);
                if (taskImprovement != null) {
                    // Atualizar os campos do taskImprovement se existir
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
                    // Adicione outras atualizações de campos de taskImprovement conforme necessário
                    task.setTaskImprovement(taskImprovement);
                    taskImprovementRepository.save(taskImprovement);
                }
            }
            if (data.taskBug() != null) {
                TaskBug taskBug = taskBugRepository.findById(data.taskBug().id()).orElse(null);
                if (taskBug != null) {
                    // Atualizar os campos do taskBug se existir
                    if (data.taskBug().checkFront() != null) {
                        taskBug.setCheckFront(data.taskBug().checkFront());
                    }
                    if (data.taskBug().checkBack() != null) {
                        taskBug.setCheckBack(data.taskBug().checkBack());
                    }
                    if (data.taskBug().checkTest() != null) {
                        taskBug.setCheckTest(data.taskBug().checkTest());
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
                System.out.println("DATA ASSIGNEE:" + data.assigneies());
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



    @PutMapping("type/requirement/{id}/check")
    public ResponseEntity<?> updateTaskRequirementCheck(@PathVariable String id , @RequestBody TaskCheckRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            TaskRequirement task = taskRequirementRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.checkProject() != null) {
                task.setCheckProject(data.checkProject());
            }

            if (data.checkRequirement() != null) {
                task.setCheckRequirement(data.checkRequirement());
            }

            if (data.checkFront() != null) {
                task.setCheckFront(data.checkFront());
            }

            if (data.checkBack() != null) {
                task.setCheckBack(data.checkBack());
            }

            if (data.checkTest() != null) {
                task.setCheckTest(data.checkTest());
            }

            taskRequirementRepository.save(task);
            response.put("data_id:", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("type/improvement/{id}/check")
    public ResponseEntity<?> updateTaskImprovementCheck(@PathVariable String id , @RequestBody TaskCheckRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            TaskImprovement task = taskImprovementRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.checkProject() != null) {
                task.setCheckProject(data.checkProject());
            }

            if (data.checkRequirement() != null) {
                task.setCheckRequirement(data.checkRequirement());
            }

            if (data.checkFront() != null) {
                task.setCheckFront(data.checkFront());
            }

            if (data.checkBack() != null) {
                task.setCheckBack(data.checkBack());
            }

            if (data.checkTest() != null) {
                task.setCheckTest(data.checkTest());
            }

            taskImprovementRepository.save(task);
            response.put("data_id:", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("type/bug/{id}/check")
    public ResponseEntity<?> updateTaskBugCheck(@PathVariable String id , @RequestBody TaskCheckRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            TaskBug task = taskBugRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            if (data.checkFront() != null) {
                task.setCheckFront(data.checkFront());
            }

            if (data.checkBack() != null) {
                task.setCheckBack(data.checkBack());
            }

            if (data.checkTest() != null) {
                task.setCheckTest(data.checkTest());
            }

            taskBugRepository.save(task);
            response.put("data_id:", task.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
}


