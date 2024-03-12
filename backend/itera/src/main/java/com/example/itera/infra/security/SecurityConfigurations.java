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
                        .requestMatchers(HttpMethod.GET,  "/acao").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/acao/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/acao/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/acao").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/acao/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/equipe").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/equipe/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/equipe/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/equipe").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/equipe/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/papel").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/papel/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/papel/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/papel").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/papel/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/projeto").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/projeto/{nome}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/projeto/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/projeto/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/projeto").permitAll()
                        .requestMatchers(HttpMethod.DELETE,  "/projeto/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisito").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/requisito/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/requisito/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/requisito").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/requisito/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requisitoNaoFuncional").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/requisitoNaoFuncional/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/requisitoNaoFuncional/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/requisitoNaoFuncional").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/requisitoNaoFuncional/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/risco").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/risco/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT,  "/risco/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,  "/risco").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,  "/risco/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
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
