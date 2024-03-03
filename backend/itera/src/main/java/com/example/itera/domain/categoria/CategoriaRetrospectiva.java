package com.example.itera.domain.categoria;

import com.example.itera.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRetrospectiva extends Categoria{
    private List<User> participantes;
    private String pontosFortes;
    private String pontosFracos;
    private String melhorias;

}
