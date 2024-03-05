package com.example.itera.domain.user;

import com.example.itera.domain.equipe.Equipe;
import com.example.itera.domain.projeto.Projeto;
import com.example.itera.domain.tarefa.Tarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    private String password;

    @Column(name = "valorhora")
    private Double valorHora;

    @Column(name = "horasdedicada")
    private Integer horasDedicada;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    private Equipe equipe;
   /* @ManyToOne
    @JoinColumn(name = "tarefa_id")  // Nome da coluna de chave estrangeira em User
    private Tarefa tarefa;*/


    public User(String nome, String email, String username, String password, Double valorHora, Integer horasDedicada, UserRole role) {
        this.nome = nome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.valorHora = valorHora;
        this.horasDedicada = horasDedicada;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
