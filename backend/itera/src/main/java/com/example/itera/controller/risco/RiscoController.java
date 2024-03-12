package com.example.itera.controller.risco;


import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.domain.risco.Risco;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalRequestDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalResponseDTO;
import com.example.itera.dto.risco.RiscoRequestDTO;
import com.example.itera.dto.risco.RiscoResponseDTO;
import com.example.itera.repository.requisitoNaoFuncional.RequisitoNaoFuncionalRepository;
import com.example.itera.repository.risco.RiscoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("risco")
public class RiscoController {

    @Autowired
    private RiscoRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRisco(@RequestBody RiscoRequestDTO data){
        Risco riscoData = new Risco(data);
        repository.save(riscoData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RiscoResponseDTO> getAll(){
        List<RiscoResponseDTO> riscoList = repository.findAll().stream().map(RiscoResponseDTO::new).toList();
        return riscoList;
    }

    @GetMapping("/{id}")
    public RiscoResponseDTO getRiscoById(@PathVariable Long id) {
        Risco risco = repository.findById(id).orElseThrow();
        if (risco != null) {
            return new RiscoResponseDTO(risco);
        } else {
            return new RiscoResponseDTO(new Risco());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisco(@PathVariable Long id) {
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


