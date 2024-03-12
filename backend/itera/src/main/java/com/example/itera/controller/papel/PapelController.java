package com.example.itera.controller.papel;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import com.example.itera.dto.papel.PapelRequestDTO;
import com.example.itera.dto.papel.PapelResponseDTO;
import com.example.itera.repository.papel.PapelRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("papel")
public class PapelController {

    @Autowired
    private PapelRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void savePapel(@RequestBody PapelRequestDTO data){
        Papel papelData = new Papel(data);
        repository.save(papelData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<PapelResponseDTO> getAll(){
        List<PapelResponseDTO> papelList = repository.findAll().stream().map(PapelResponseDTO::new).toList();
        return papelList;
    }

    @GetMapping("/{id}")
    public PapelResponseDTO getPapelById(@PathVariable Long id) {
        Papel papel = repository.findById(id).orElseThrow();
        if (papel != null) {
            return new PapelResponseDTO(papel);
        } else {
            return new PapelResponseDTO(new Papel());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAPapel(@PathVariable Long id) {
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


