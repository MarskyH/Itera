package com.example.itera.controller.equipe;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import com.example.itera.repository.equipe.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}


