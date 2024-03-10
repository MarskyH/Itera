package com.example.itera.controller.requisito;



import com.example.itera.domain.requisito.Requisito;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import com.example.itera.repository.requisito.RequisitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}


