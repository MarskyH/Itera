package com.example.itera.domain.requisito;
import com.example.itera.domain.projeto.Projeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "requisito")
@Entity(name = "requisito")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Requisito {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String titulo;
    private String detalhamento;
    private String complexidade;
    private String prioridade;
    private Integer esforco;
    private Integer tamanho;
    /*@ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;*/
}
