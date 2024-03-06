package com.example.itera.controller.papel;


import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.papel.Papel;
import com.example.itera.dto.equipe.EquipeRequestDTO;
import com.example.itera.dto.equipe.EquipeResponseDTO;
import com.example.itera.dto.papel.PapelRequestDTO;
import com.example.itera.dto.papel.PapelResponseDTO;
import com.example.itera.repository.papel.PapelRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}


