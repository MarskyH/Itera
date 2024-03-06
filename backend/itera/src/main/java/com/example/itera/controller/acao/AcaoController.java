package com.example.itera.controller.acao;

import com.example.itera.domain.acao.Acao;
import com.example.itera.dto.acao.AcaoRequestDTO;
import com.example.itera.dto.acao.AcaoResponseDTO;
import com.example.itera.repository.acao.AcaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<AcaoResponseDTO> getAll(){
        List<AcaoResponseDTO> acaoList = repository.findAll().stream().map(AcaoResponseDTO::new).toList();
        return acaoList;
    }
}


