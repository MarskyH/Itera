package com.example.itera.controller.taskType;

import com.example.itera.dto.degree.DegreeResponseDTO;
import com.example.itera.dto.taskType.TaskTypeResponseDTO;
import com.example.itera.repository.degree.DegreeRepository;
import com.example.itera.repository.taskType.TaskTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Responsável por fornecer endpoints para manipulação de grau de exposição
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */
@RestController
@RequestMapping("tasktype")
public class TaskTypeController {

    @Autowired
    TaskTypeRepository repository;


    /**
     * Endpoint responsável por retornar lista de tipos de tarefas, buscando pelo seu identificador.
     * @return Lista no formato DegreeResponseDTO
     * @author Marcus Loureiro
     * @see TaskTypeResponseDTO
     * @since 18/04/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TaskTypeResponseDTO> getAll()  {
        return repository.findAll().stream().map(TaskTypeResponseDTO::new).toList();
    }

}