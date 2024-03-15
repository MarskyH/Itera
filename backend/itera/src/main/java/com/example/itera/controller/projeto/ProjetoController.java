package com.example.itera.controller.projeto;



import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.user.User;
import com.example.itera.dto.papel.PapelResponseDTO;
import com.example.itera.dto.projeto.ProjetoCompletoResponseDTO;
import com.example.itera.dto.projeto.ProjetoRequestDTO;
import com.example.itera.dto.projeto.ProjetoResponseDTO;
import com.example.itera.dto.requisito.RequisitoResponseDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalResponseDTO;
import com.example.itera.dto.risco.RiscoResponseDTO;
import com.example.itera.infra.security.TokenService;
import com.example.itera.repository.acao.AcaoRepository;
import com.example.itera.repository.equipe.EquipeRepository;
import com.example.itera.repository.papel.PapelRepository;
import com.example.itera.repository.projeto.ProjetoRepository;
import com.example.itera.repository.requisito.RequisitoRepository;
import com.example.itera.repository.requisitoNaoFuncional.RequisitoNaoFuncionalRepository;
import com.example.itera.repository.risco.RiscoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("projeto")
public class ProjetoController {

    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    RiscoRepository riscoRepository;

    @Autowired
    AcaoRepository acapRepository;

    @Autowired
    RequisitoRepository requisitoRepository;

    @Autowired
    RequisitoNaoFuncionalRepository requisitoNaoFuncionalRepository;


    @Autowired
    TokenService tokenService;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping
    public void saveProjeto(@RequestBody ProjetoRequestDTO data){
        String username = tokenService.validateToken(SecurityContextHolder.getContext().getAuthentication().getName());
        Projeto projetoData = new Projeto(data);
        projetoData.setCreatedBy(username);
        projetoRepository.save(projetoData);
        return;
    }

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @GetMapping
    public List<ProjetoResponseDTO> getAll(){
        List<ProjetoResponseDTO> projetoList = projetoRepository.findAll().stream().map(ProjetoResponseDTO::new).toList();
        return projetoList;
    }

    @GetMapping("/id/{id}")
    public ProjetoResponseDTO getProjetoById(@PathVariable Long id) {
        Projeto projeto = projetoRepository.findById(id).orElseThrow();
        if (projeto != null) {
            return new ProjetoResponseDTO(projeto);
        } else {
            return new ProjetoResponseDTO(new Projeto());
        }
    }

    @GetMapping("/nome/{nome}")
    public ProjetoResponseDTO getProjetoByNome(@PathVariable String nome) {
        Projeto projeto = projetoRepository.findByNome(nome);
        if (projeto != null){
            return new ProjetoResponseDTO(projeto);
        }else{
            return new ProjetoResponseDTO(new Projeto());
        }
    }

    @GetMapping("/completo/{nome}")
    public ProjetoCompletoResponseDTO getProjetoCompleto(@PathVariable String nome) {
        Projeto projeto = projetoRepository.findByNome(nome);
        if (projeto != null){
            List<PapelResponseDTO> listPapel = papelRepository.findByProjeto(projeto.getId());
            Equipe equipe = equipeRepository.findByProjeto(projeto.getId());
            List<User> listaUsersEquipe = equipeRepository.findByUsersEquipe(equipe.getId());
            List<RiscoResponseDTO> listaRiscos = riscoRepository.findByProjeto(projeto.getId());
            //List<AcaoResponseDTO> listaAcoes = acapRepository.findByProjeto(projeto.getId());
            List<RequisitoResponseDTO> listaRequisitos = requisitoRepository.findByProjeto(projeto.getId());
            List<RequisitoNaoFuncionalResponseDTO> listaRequisitosNaoFuncionais = requisitoNaoFuncionalRepository.findByProjeto(projeto.getId());


            return new ProjetoCompletoResponseDTO(projeto, listPapel,listaUsersEquipe, listaRiscos, listaRequisitos, listaRequisitosNaoFuncionais);
        }else{
            return new ProjetoCompletoResponseDTO(new Projeto(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjeto(@PathVariable Long id) {
        try {
            projetoRepository.delete(projetoRepository.getReferenceById(id));
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}


