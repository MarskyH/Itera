package com.example.itera.controller;


import com.example.itera.domain.user.AuthenticationDTO;
import com.example.itera.domain.user.LoginResponseDTO;
import com.example.itera.domain.user.RegisterDTO;
import com.example.itera.domain.user.User;
import com.example.itera.infra.security.TokenService;
import com.example.itera.domain.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token  = tokenService.generateToken((User) auth.getPrincipal());
        ResponseCookie cookie = ResponseCookie.from("Token", token)
                .httpOnly(false)  // Prevent client-side JavaScript access
                .secure(false)   // Send only over HTTPS (if applicable)
                .path("/")
                .maxAge(60 * 60 * 3)
                .build(); // Set expiration time (3 hours)

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponseDTO(token));  // Optional: Include token in response body as well
    }
    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.nome(), data.login(), encryptedPassword, data.horasDedicada(), data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
