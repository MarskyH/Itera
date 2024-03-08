package com.example.itera.domain.requisitoNaoFuncional;

import com.example.itera.domain.projeto.Projeto;
import com.example.itera.dto.requisito.RequisitoRequestDTO;
import com.example.itera.dto.requisitoNaoFuncional.RequisitoNaoFuncionalRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "requisitonaofuncional")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@SequenceGenerator(name = "requisitonaofuncionalseq", sequenceName = "requisitonaofuncional_seq", allocationSize = 1)
public class RequisitoNaoFuncional {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "requisitonaofuncional_seq")
    private Long id;
    private String titulo;
    private Integer valor;

    @ManyToOne
    private Projeto projeto;
    public RequisitoNaoFuncional(RequisitoNaoFuncionalRequestDTO data){
        this.titulo = data.titulo();
        this.valor = data.valor();
        this.projeto = data.projeto();

    }

}

