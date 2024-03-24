package com.example.itera.controller.degree;

import com.example.itera.dto.degree.DegreeResponseDTO;
import com.example.itera.repository.degree.DegreeRepository;
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
@RequestMapping("degree")
public class DegreeController {

    @Autowired
    DegreeRepository repository;


    /**
     * Endpoint responsável por retornar um requisito específico, buscando pelo seu identificador.
     * @return Lista no formato DegreeResponseDTO
     * @author Marcus Loureiro
     * @see DegreeResponseDTO
     * @since 23/03/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<DegreeResponseDTO> getAll()  {
        return repository.findAll().stream().map(DegreeResponseDTO::new).toList();
    }

}