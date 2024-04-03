package com.example.itera.controller.priority;

import com.example.itera.dto.degree.DegreeResponseDTO;
import com.example.itera.dto.priority.PriorityResponseDTO;
import com.example.itera.repository.degree.DegreeRepository;
import com.example.itera.repository.priority.PriorityRepository;
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
@RequestMapping("priority")
public class PriorityController {

    @Autowired
    PriorityRepository repository;


    /**
     * Endpoint responsável por retornar uma lista de prioridades usadas para os reqyisitos funcionais.
     * @return Lista no formato PriorityResponseDTO
     * @author Marcus Loureiro
     * @see PriorityResponseDTO
     * @since 23/03/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PriorityResponseDTO> getAll()  {
        return repository.findAll().stream().map(PriorityResponseDTO::new).toList();
    }

}