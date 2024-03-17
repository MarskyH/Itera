package com.example.itera.domain.user;

import com.example.itera.domain.team.Team;
import com.example.itera.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    @Column(name = "dedicated_hours")
    private Integer dedicatedHours;

    @Column(name = "user_role")
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "team_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "role_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Role role;


    public User(String name, String email, String login, String password, Double hourlyRate, Integer dedicatedHours, UserRole userRole) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.hourlyRate = hourlyRate;
        this.dedicatedHours = dedicatedHours;
        this.userRole = userRole;
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
