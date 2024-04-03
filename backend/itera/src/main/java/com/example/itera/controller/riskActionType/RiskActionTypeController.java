package com.example.itera.controller.riskActionType;

import com.example.itera.dto.risk.RiskResponseDTO;
import com.example.itera.dto.riskActionType.RiskActionTypeResponseDTO;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.repository.riskActionType.RiskActionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Responsável por fornecer endpoints para manipulação de tipos de ação para riscos
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */
@RestController
@RequestMapping("riskActionType")
public class RiskActionTypeController {

    @Autowired
    RiskActionTypeRepository repository;


    /**
     * Endpoint responsável por retornar um requisito específico, buscando pelo seu identificador.
     * @return Lista no formato RiskActionTypeResponseDTO
     * @author Marcus Loureiro
     * @see RiskActionTypeResponseDTO
     * @since 23/03/2024
     */

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RiskActionTypeResponseDTO> getAll()  {
        return repository.findAll().stream().map(RiskActionTypeResponseDTO::new).toList();
    }

}