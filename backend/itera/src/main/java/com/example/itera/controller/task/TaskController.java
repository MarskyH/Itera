package com.example.itera.controller.task;


import com.example.itera.domain.Assignee.Assignee;
import com.example.itera.domain.iteration.Iteration;
import com.example.itera.domain.task.Task;
import com.example.itera.domain.taskBug.TaskBug;
import com.example.itera.domain.taskImprovement.TaskImprovement;
import com.example.itera.domain.taskRequirement.TaskRequirement;
import com.example.itera.domain.user.User;
import com.example.itera.dto.assignee.AssigneeRequestDTO;
import com.example.itera.dto.task.*;
import com.example.itera.dto.taskBug.TaskBugRequestDTO;
import com.example.itera.dto.taskImprovement.TaskImprovementRequestDTO;
import com.example.itera.dto.taskRequirement.TaskRequirementRequestDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.assignee.AssigneeRepository;
import com.example.itera.repository.iteration.IterationRepository;
import com.example.itera.repository.task.TaskRepository;
import com.example.itera.repository.taskBug.TaskBugRepository;
import com.example.itera.repository.taskImprovement.TaskImprovementRepository;
import com.example.itera.repository.taskRequirement.TaskRequirementRepository;
import com.example.itera.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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

    @Autowired
    IterationRepository iterationRepository;

    @Autowired
    UserRepository userRepository;

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
        TaskRequirementRequestDTO dataTaskRequirement = data.taskRequirement();
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
                    dataTaskRequirement.details(),
                    dataTaskRequirement.complexity(),
                    dataTaskRequirement.sizeTask(),
                    dataTaskRequirement.effort(),
                    taskData
            );
            taskRequirementRepository.save(taskRequirementData);

            taskData.setTaskRequirement(taskRequirementData);
            taskRepository.save(taskData);

            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    User userData = userRepository.findById(assignee.user_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), userData, taskData);
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
            TaskRequestRequirementDTO dataTask = data.task();
            TaskImprovementRequestDTO dataTaskImprovement = data.taskImprovement();

            Iteration iterationData = iterationRepository.findById(dataTask.iteration_id()).orElseThrow();
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
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
                    dataTaskImprovement.details(),
                    dataTaskImprovement.complexity(),
                    dataTaskImprovement.sizeTask(),
                    dataTaskImprovement.effort(),
                    taskData
            );
            taskImprovementRepository.save(taskImprovementData);

            taskData.setTaskImprovement(taskImprovementData);
            taskRepository.save(taskData);

            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    User userData = userRepository.findById(assignee.user_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), userData, taskData);
                    assigneeRepository.save(assigneeData);
                    response.put("Assignee_id", assigneeData.getId());
                }
            }

            response.put("task_id", taskData.getId());
            response.put("taskImprovement_id", taskImprovementData.getId());
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
            TaskRequestRequirementDTO dataTask = data.task();
            TaskBugRequestDTO dataTaskBug = data.taskBug();

            Iteration iterationData = iterationRepository.findById(dataTask.iteration_id()).orElseThrow();
            Task taskData = new Task(
                    dataTask.title(),
                    dataTask.priority(),
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
                    dataTaskBug.details(),
                    dataTaskBug.complexity(),
                    dataTaskBug.sizeTask(),
                    dataTaskBug.effort(),
                    taskData
            );
            taskBugRepository.save(taskBugData);

            taskData.setTaskBug(taskBugData);
            taskRepository.save(taskData);
            if(data.assignees() != null){
                for (AssigneeRequestDTO assignee : data.assignees()) {
                    User userData = userRepository.findById(assignee.user_id()).orElseThrow();
                    Assignee assigneeData = new Assignee(assignee.taskStep(), userData, taskData);
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
    @PutMapping("/{id}/type/requirement")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody TaskRequestRequirementDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.taskrequirement_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskrequirement_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if (data.taskrequirement_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskrequirement_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if(data.listName() != null && !data.listName().equals(task.getListName())){
                task.setListName(data.listName());
            }

            if(data.orderTask() != null){
                task.setOrderTask(data.orderTask());
            }

            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }
            taskRepository.save(task);
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
    @PutMapping("/{id}/type/improvement")
    public ResponseEntity<?> updateTaskImprovement(@PathVariable String id, @RequestBody TaskRequestImprovementDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.taskimprovement_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskimprovement_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if(data.listName() != null && !data.listName().equals(task.getListName())){
                task.setListName(data.listName());
            }

            if(data.orderTask() != null){
                task.setOrderTask(data.orderTask());
            }

            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }
            taskRepository.save(task);
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
    @PutMapping("/{id}/type/bug")
    public ResponseEntity<?> updateTaskBug(@PathVariable String id, @RequestBody TaskRequestBugDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.taskbug_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskbug_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if(data.listName() != null && !data.listName().equals(task.getListName())){
                task.setListName(data.listName());
            }

            if(data.orderTask() != null){
                task.setOrderTask(data.orderTask());
            }

            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }
            taskRepository.save(task);
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
/*
    @PutMapping("/type/requirement/{id}")
    public ResponseEntity<?> updateTaskRequirement(@PathVariable String id, @RequestBody TaskTaskRequirementRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Task task = taskRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                task.setTitle(data.title());
            }
            if (data.priority() != null) {
                task.setPriority(data.priority());
            }
            if (data.startDate() != null) {
                task.setStartDate(data.startDate());
            }
            if (data.endDate() != null) {
                task.setEndDate(data.endDate());
            }
            if (data.taskrequirement_id() != null) {
                TaskRequirement taskRequirement = taskRequirementRepository.findById(data.taskrequirement_id()).orElseThrow();
                task.setTaskRequirement(taskRequirement);
            }
            if (data.iteration_id() != null) {
                Iteration iteration = iterationRepository.findById(data.iteration_id()).orElseThrow();
                task.setIteration(iteration);
            }
            taskRepository.save(task);
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
*/

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


