package com.example.itera.controller.requisito;



import com.example.itera.domain.requisito.Requisito;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import com.example.itera.repository.requisito.RequisitoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requisito")
public class RequisitoController {

    @Autowired
    private RequisitoRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRequisito(@RequestBody RequisitoRequestDTO data){
        Requisito requisitoData = new Requisito(data);
        repository.save(requisitoData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RequisitoResponseDTO> getAll(){
        List<RequisitoResponseDTO> requisitoList = repository.findAll().stream().map(RequisitoResponseDTO::new).toList();
        return requisitoList;
    }

    @GetMapping("/{id}")
    public RequisitoResponseDTO getRequisitoById(@PathVariable Long id) {
        Requisito requisito = repository.findById(id).orElseThrow();
        if (requisito != null) {
            return new RequisitoResponseDTO(requisito);
        } else {
            return new RequisitoResponseDTO(new Requisito());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequisito(@PathVariable Long id) {
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


