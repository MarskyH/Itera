package com.example.itera.domain.categoria;

import com.example.itera.domain.responsavel.Responsavel;
import com.example.itera.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRevisao extends Categoria{
    private List<Responsavel> backlogConcluido;
    private List<Responsavel> backlogSelecionado;
    private List<User> participantes;
    private Integer totalTamanhoConcluido;
    private Integer totalEsforcoConcluido;
    private Integer velocidadeConcluida;
    private Boolean checkEscopo;
    private Boolean checkRecursosHumanos;
    private Boolean checkVelocidade;
    private Boolean checkRiscos;
}
