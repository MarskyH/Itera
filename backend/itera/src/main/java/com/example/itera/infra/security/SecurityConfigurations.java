package com.example.itera.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,  "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/acao").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/acao/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/acao/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/acao").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/acao/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/equipe").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/equipe/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/equipe//users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/equipe/projeto/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/equipe/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/equipe").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/equipe/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/papel").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/papel/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/papel/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/papel").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/papel/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/projeto").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/projeto/nome/{nome}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/projeto/id/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/projeto/completo/{nome}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/projeto/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/projeto").permitAll()
                        .requestMatchers(HttpMethod.DELETE,  "/projeto/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisito").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisito/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/requisito/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/requisito").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/requisito/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisitoNaoFuncional").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisitoNaoFuncional/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/requisitoNaoFuncional/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/requisitoNaoFuncional").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/requisitoNaoFuncional/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/risco").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/risco/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/risco/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/risco").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/risco/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/atualizar/equipe").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
}
