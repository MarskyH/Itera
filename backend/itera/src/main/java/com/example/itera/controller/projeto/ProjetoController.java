package com.example.itera.controller.projeto;


import com.example.itera.domain.papel.Papel;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.papel.PapelRequestDTO;
import com.example.itera.dto.papel.PapelResponseDTO;
import com.example.itera.dto.projeto.ProjetoRequestDTO;
import com.example.itera.dto.projeto.ProjetoResponseDTO;
import com.example.itera.repository.papel.PapelRepository;
import com.example.itera.repository.projeto.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void savePapel(@RequestBody ProjetoRequestDTO data){
        Projeto projetoData = new Projeto(data);
        repository.save(projetoData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<ProjetoResponseDTO> getAll(){
        List<ProjetoResponseDTO> projetoList = repository.findAll().stream().map(ProjetoResponseDTO::new).toList();
        return projetoList;
    }
}


