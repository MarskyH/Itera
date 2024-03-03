package com.example.itera.domain.categoria;

import com.example.itera.domain.requisito.Requisito;
import com.example.itera.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaPlanejamento extends Categoria{
    private List<Requisito> backlogSelecionado;
    private List<User> recursosHumanosSelecionados;
    private Integer totalTamanhoPlanejado;
    private Integer totalEsforcoPlanejado;
    private Integer velocidadePlanejada;
}
