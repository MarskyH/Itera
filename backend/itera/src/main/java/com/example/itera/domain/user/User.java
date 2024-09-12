package com.example.itera.domain.user;


import com.example.itera.dto.user.RegisterDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "username")
    private String login;
    private String password;
    @Column(name = "user_role")
    private UserRole userRole;

    public User(String name, String email, String login, String password, UserRole userRole) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public User(String name, String email, String login, UserRole userRole) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.userRole = userRole;
    }

    public User(RegisterDTO data){
        this.name = data.name();
        this.login = data.login();
        this.email = data.email();
        this.password = data.password();
        this.userRole = data.userRole();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.userRole == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
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
