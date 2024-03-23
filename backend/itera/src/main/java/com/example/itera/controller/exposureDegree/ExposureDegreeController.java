package com.example.itera.controller.exposureDegree;

import com.example.itera.dto.exposureDegree.ExposureDegreeResponseDTO;
import com.example.itera.repository.exposureDegree.ExposureDegreeRepository;
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
@RequestMapping("exposureDegree")
public class ExposureDegreeController {

    @Autowired
    ExposureDegreeRepository repository;


    /**
     * Endpoint responsável por retornar um requisito específico, buscando pelo seu identificador.
     * @return Lista no formato RiskActionTypeResponseDTO
     * @author Marcus Loureiro
     * @see ExposureDegreeResponseDTO
     * @since 23/03/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ExposureDegreeResponseDTO> getAll()  {
        return repository.findAll().stream().map(ExposureDegreeResponseDTO::new).toList();
    }

}