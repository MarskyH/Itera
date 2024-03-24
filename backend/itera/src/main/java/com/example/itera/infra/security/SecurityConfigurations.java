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
                        .requestMatchers(HttpMethod.GET,  "/activity").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/activity/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/activity/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/activity").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/activity/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/team").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/team/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/team/{id}/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/team/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/team/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/team").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/teamMember").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/teamMember/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/teamMember/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/teamMember/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/teamMember").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/team/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/role/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/role/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/role/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/role/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/name/{name}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}/roles").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}/teamMembers").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}/risks").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}/requirements").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/{id}/nonFunctionalRequirement").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/user/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/project/completo/{name}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/project").permitAll()
                        .requestMatchers(HttpMethod.DELETE,  "/project/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requirement").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/requirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/requirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/requirement").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/requirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/nonFunctionalRequirement").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/nonFunctionalRequirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/nonFunctionalRequirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/nonFunctionalRequirement").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/nonFunctionalRequirement/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/risk").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/risk/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,  "/risk/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,  "/risk").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,  "/risk/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/team").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,  "/riskActionType").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,  "/degree").hasRole("ADMIN")
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
