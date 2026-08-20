package br.com.solarquote.controller;

import br.com.solarquote.config.JwtService;
import br.com.solarquote.dto.AuthRequestDTO;
import br.com.solarquote.dto.AuthResponseDTO;
import br.com.solarquote.entity.Usuario;
import br.com.solarquote.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            JwtService jwtService) {

        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthRequestDTO dados) {

        var usuario = authService.autenticar(
                dados.getEmail(),
                dados.getSenha()
        );

        if (usuario.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou senha inválidos");
        }

        Usuario usuarioAutenticado = usuario.get();

        String token = jwtService.gerarToken(
                usuarioAutenticado
        );

        AuthResponseDTO resposta = new AuthResponseDTO(
                "Login realizado com sucesso",
                usuarioAutenticado.getId(),
                usuarioAutenticado.getNome(),
                usuarioAutenticado.getEmail(),
                token
        );

        return ResponseEntity.ok(resposta);
    }
}