package com.example.itera.domain.categoria;

import com.example.itera.domain.responsavel.Responsavel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaBug extends Categoria{
    private String descricao;
    private String complexidade;
    private String esforco;
    private String tamanho;
    private List<Responsavel> listaResponsaveis;
}
