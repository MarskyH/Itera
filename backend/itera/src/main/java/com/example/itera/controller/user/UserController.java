package com.example.itera.controller.user;

import com.example.itera.domain.acao.Acao;
import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.risco.Risco;
import com.example.itera.domain.user.User;
import com.example.itera.dto.acao.AcaoRequestDTO;
import com.example.itera.dto.user.UserEquipeRequestDTO;
import com.example.itera.repository.equipe.EquipeRepository;
import com.example.itera.repository.projeto.ProjetoRepository;
import com.example.itera.repository.user.UserRepository;
import com.example.itera.dto.user.UserResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<UserResponseDTO> getAll(){
        List<UserResponseDTO> userList = userRepository.findAll().stream().map(UserResponseDTO::new).toList();
        return userList;
    }

   /* @PutMapping("/atualizar/equipe/{nomeUser}/{nomeProjeto}")
    public ResponseEntity<Void> updateEquipeUser(@PathVariable String nomeUser, @PathVariable String nomeProjeto) {
       System.out.println("nome user:" + nomeUser);
       System.out.println("nome projeto:" + nomeProjeto);

        try {
            User dataUser = userRepository.findByNome(nomeUser);
            Projeto dataProjeto = projetoRepository.findByNome(nomeProjeto);
            Equipe dataEquipe = equipeRepository.findByProjeto(dataProjeto.getId());
            dataUser.setEquipe(dataEquipe);
            userRepository.save(dataUser);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } */

    @PutMapping("/atualizar/equipe")
    public ResponseEntity<Void> updateEquipeUser(@RequestBody UserEquipeRequestDTO data) {
        System.out.println("nome user:" + data.nomeUser());
        System.out.println("nome projeto:" + data.nomeProjeto());
        try {
            User dataUser = userRepository.findByNome(data.nomeUser());
            Projeto dataProjeto = projetoRepository.findByNome(data.nomeProjeto());
            Equipe dataEquipe = equipeRepository.findByProjeto(dataProjeto.getId());
            dataUser.setEquipe(dataEquipe);
            userRepository.save(dataUser);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}