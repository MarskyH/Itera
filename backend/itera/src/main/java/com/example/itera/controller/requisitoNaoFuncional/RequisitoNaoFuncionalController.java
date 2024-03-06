package com.example.itera.controller.requisitoNaoFuncional;


import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.requisitoNaoFuncional.RequisitoNaoFuncional;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalRequestDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalResponseDTO;
import com.example.itera.repository.requisito.RequisitoRepository;
import com.example.itera.repository.requisitoNaoFuncional.RequisitoNaoFuncionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("requisitoNaoFuncional")
public class RequisitoNaoFuncionalController {

    @Autowired
    private RequisitoNaoFuncionalRepository repository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveRequisitoNaoFuncional(@RequestBody RequisitoNaoFuncionalRequestDTO data){
        RequisitoNaoFuncional requisitoNaoFuncionalData = new RequisitoNaoFuncional(data);
        repository.save(requisitoNaoFuncionalData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<RequisitoNaoFuncionalResponseDTO> getAll(){
        List<RequisitoNaoFuncionalResponseDTO> requisitoNaoFuncionalList = repository.findAll().stream().map(RequisitoNaoFuncionalResponseDTO::new).toList();
        return requisitoNaoFuncionalList;
    }
}


