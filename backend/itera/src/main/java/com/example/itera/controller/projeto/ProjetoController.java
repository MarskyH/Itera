package com.example.itera.controller.projeto;



import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.projeto.ProjetoRequestDTO;
import com.example.itera.dto.projeto.ProjetoResponseDTO;
import com.example.itera.infra.security.TokenService;
import com.example.itera.repository.projeto.ProjetoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository repository;

    @Autowired
    TokenService tokenService;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveProjeto(@RequestBody ProjetoRequestDTO data){
        String username = tokenService.validateToken(SecurityContextHolder.getContext().getAuthentication().getName());
        Projeto projetoData = new Projeto(data);
        System.out.println("USERNAME:" + username);
        projetoData.setCreatedBy(username);
        repository.save(projetoData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<ProjetoResponseDTO> getAll(){
        List<ProjetoResponseDTO> projetoList = repository.findAll().stream().map(ProjetoResponseDTO::new).toList();
        return projetoList;
    }

    @GetMapping("/{id}")
    public ProjetoResponseDTO getProjetoById(@PathVariable Long id) {
        Projeto projeto = repository.findById(id).orElseThrow();
        if (projeto != null) {
            return new ProjetoResponseDTO(projeto);
        } else {
            return new ProjetoResponseDTO(new Projeto());
        }
    }

    @GetMapping("/{nome}")
    public ProjetoResponseDTO getProjetoByNome(@PathVariable String nome) {
        Projeto projeto = repository.findByNome(nome).orElseThrow();
        if (projeto != null){
            return new ProjetoResponseDTO(projeto);
        }else{
            return new ProjetoResponseDTO(new Projeto());
        }
    }

    @GetMapping("/completo/{id}")
    public ProjetoResponseDTO getProjetoCompleto(@PathVariable String nome) {
        Projeto projeto = repository.findByNome(nome).orElseThrow();
        if (projeto != null){
            return new ProjetoResponseDTO(projeto);
        }else{
            return new ProjetoResponseDTO(new Projeto());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjeto(@PathVariable Long id) {
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


