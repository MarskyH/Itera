package com.example.itera.controller.auth;


import com.example.itera.dto.auth.AuthenticationDTO;
import com.example.itera.dto.auth.LoginResponseDTO;
import com.example.itera.dto.user.RegisterDTO;
import com.example.itera.domain.user.User;
import com.example.itera.infra.security.TokenService;
import com.example.itera.repository.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        try {
            if (this.repository.findByLogin(data.login()) != null) {
                // Return a specific error message for duplicate login
                return ResponseEntity
                        .badRequest()
                        .body("Login já existe"); // Or a custom error object for more detail
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.name(), data.email(), data.login(), encryptedPassword, data.userRole());
            this.repository.save(newUser);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            // Handle any unexpected errors
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar usuário"); // Or a more informative error message
        }
    }
}
