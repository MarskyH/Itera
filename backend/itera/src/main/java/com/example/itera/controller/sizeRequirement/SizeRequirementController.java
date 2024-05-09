package com.example.itera.controller.sizeRequirement;

import com.example.itera.domain.sizeRequirement.SizeRequirement;
import com.example.itera.dto.sizeRequirement.SizeRequirementResponseDTO;
import com.example.itera.dto.taskType.TaskTypeResponseDTO;
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
@RequestMapping("sizerequirement")
public class SizeRequirementController {

    @Autowired
    SizeRequirement repository;


    /**
     * Endpoint responsável por retornar lista de tamanhos de requisitos funcionais, buscando pelo seu identificador.
     * @return Lista no formato SizeRequirementResponseDTO
     * @author Marcus Loureiro
     * @see SizeRequirementResponseDTO
     * @since 18/04/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<SizeRequirementResponseDTO> getAll()  {
        return repository.findAll().stream().map(TaskTypeResponseDTO::new).toList();
    }

}