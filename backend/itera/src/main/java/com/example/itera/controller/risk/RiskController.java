package com.example.itera.controller.risk;

import com.example.itera.domain.project.Project;
import com.example.itera.domain.risk.Risk;
import com.example.itera.dto.risk.RiskRequestDTO;
import com.example.itera.dto.risk.RiskResponseDTO;
import com.example.itera.enumeration.ResponseType;
import com.example.itera.exception.ResourceNotFoundException;
import com.example.itera.exception.UnauthorizedException;
import com.example.itera.repository.project.ProjectRepository;
import com.example.itera.repository.risk.RiskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * Responsável por fornecer endpoints para manipulação de riscos
 *
 * @author Marcus Loureiro
 * @version 1.0
 * @since 23/03/2024
 */
@RestController
@RequestMapping("risk")
public class RiskController {

    @Autowired
    RiskRepository repository;

    @Autowired
    ProjectRepository projectRepository;

    /**
     * Endpoint responsável por cadastrar um risco.
     *
     * @param data estrutura de dados contendo as informações necessárias para persistir o risco
     * @return ResponseEntity confirmando a transação e retornando o ‘id’ do projeto usado e do risco criado.
     * @author Marcus Loureiro
     * @see RiskRequestDTO
     * @see ResponseType
     * @since 23/03/2024
     */

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> saveRisk(@RequestBody RiskRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Project projectData = projectRepository.findById(data.project_id()).orElseThrow();
            Risk riskData = new Risk(
                    data.title(),
                    data.effect(),
                    data.probability(),
                    data.impact(),
                    data.exposureDegree(),
                    data.description(),
                    data.type(),
                    projectData
            );
            repository.save(riskData);
            response.put("project_id:", projectData.getId());
            response.put("requirement_id:", riskData.getId());
            response.put("message", ResponseType.SUCCESS_SAVE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseType.FAIL_SAVE.getMessage());
        }
    }

    /**
     * Endpoint responsável por retornar um requisito específico, buscando pelo seu identificador.
     *
     * @param id Identificador único do risco.
     * @return Risco no formato RiskResponseDTO caso encontrado, caso contrário, retorna erro 404 (Not Found).
     * @throws ResourceNotFoundException Exceção lançada caso o risco não seja encontrado.
     * @author Marcus Loureiro
     * @see RiskResponseDTO
     * @since 23/03/2024
     */

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public RiskResponseDTO getRiskById(@PathVariable String id) throws ResourceNotFoundException {
        Risk risk = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ResponseType.EMPTY_GET.getMessage() + " id: " + id));
        return new RiskResponseDTO(risk);
    }

    /**
     * Endpoint responsável por atualizar um risco existente, identificado pelo seu ‘ID’.
     *
     * @param id   Identificador único do risco a ser atualizado.
     * @param data Objeto contendo os dados do risco a serem atualizados.
     * @return Resposta HTTP indicando o sucesso da operação ou informações sobre o erro ocorrido.
     * @throws EntityNotFoundException Exceção lançada caso o risco não seja encontrado.
     * @author Marcus Loureiro
     * @since 23/03/2024
     */

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> updateRisk(@PathVariable String id, @RequestBody RiskRequestDTO data) {
        Map<String, String> response = new HashMap<>();
        try {
            Risk risk = repository.findById(id).orElseThrow(EntityNotFoundException::new);

            // Atualizar apenas os campos fornecidos pelo utilizador
            if (data.title() != null) {
                risk.setTitle(data.title());
            }
            if (data.effect() != null) {
                risk.setEffect(data.effect());
            }
            if (data.probability() != null) {
                risk.setProbability(data.probability());
            }
            if (data.impact() != null) {
                risk.setImpact(data.impact());
            }
            if (data.exposureDegree() != null) {
                risk.setExposureDegree(data.exposureDegree());
            }
            if (data.description() != null) {
                risk.setDescription(data.description());
            }
            if (data.type() != null) {
                risk.setType(data.type());
            }

            repository.save(risk);
            response.put("data_id:", risk.getId());
            response.put("message", ResponseType.SUCCESS_UPDATE.getMessage());
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisk(@PathVariable String id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}