package com.example.itera.controller.equipe;



import com.example.itera.domain.equipe.Equipe;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import com.example.itera.dto.user.UserResponseDTO;
import com.example.itera.repository.equipe.EquipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("equipe")
public class EquipeController {

    @Autowired
    private EquipeRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveEquipe(@RequestBody EquipeRequestDTO data){
        Equipe equipeData = new Equipe(data);
        repository.save(equipeData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<EquipeResponseDTO> getAll(){
        List<EquipeResponseDTO> equipeList = repository.findAll().stream().map(EquipeResponseDTO::new).toList();
        return equipeList;
    }

    @GetMapping("/{id}")
    public EquipeResponseDTO getEquipeById(@PathVariable Long id) {
        Equipe equipe = repository.findById(id).orElseThrow();
        if (equipe != null) {
            return new EquipeResponseDTO(equipe);
        } else {
            return new EquipeResponseDTO(new Equipe());
        }
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsersEquipeById(@PathVariable Long id){
        List<UserResponseDTO> userList = repository.findByUsersEquipe(id).stream().map(UserResponseDTO::new).toList();
        return userList;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipe(@PathVariable Long id) {
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


