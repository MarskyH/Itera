package com.example.itera.controller.acao;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.risco.Risco;
import com.example.itera.dto.acao.AcaoRequestDTO;
import com.example.itera.dto.acao.AcaoResponseDTO;
import com.example.itera.repository.acao.AcaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

import java.util.List;

@RestController
@RequestMapping("acao")
public class AcaoController {

    @Autowired
    private AcaoRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveAcao(@RequestBody AcaoRequestDTO data){
        Acao acaoData = new Acao(data);
        repository.save(acaoData);
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<AcaoResponseDTO> getAll(){
        List<AcaoResponseDTO> acaoList = repository.findAll().stream().map(AcaoResponseDTO::new).toList();
        return acaoList;
    }

    @GetMapping("/{id}")
    public AcaoResponseDTO getAcaoById(@PathVariable Long id) {
        Acao acao = repository.findById(id).orElseThrow();
        if (acao != null) {
            return new AcaoResponseDTO(acao);
        } else {
            return new AcaoResponseDTO(new Acao());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAcao(@PathVariable Long id, @RequestBody AcaoRequestDTO data) {
        try {
            Acao acaoExistente = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            Acao acaoNova = new Acao(data);

            // Validar os campos da AcaoRequestDTO, se necessário

            // Atualizar a entidade usando o construtor
            acaoExistente = new Acao(
                    acaoNova.getTitulo(),
                    acaoNova.getDescricao(),
                    acaoNova.getTipo(),
                    Objects.equals(acaoNova.getRisco(), new Risco()) ? acaoExistente.getRisco() : acaoNova.getRisco()
            );

            repository.save(acaoExistente);

            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace(); // ou logar o erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcao(@PathVariable Long id) {
        try {
            repository.delete(repository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


