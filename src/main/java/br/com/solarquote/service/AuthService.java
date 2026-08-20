package br.com.solarquote.service;

import br.com.solarquote.entity.Usuario;
import br.com.solarquote.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> autenticar(
            String email,
            String senha) {

        Optional<Usuario> usuario =
                usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            return Optional.empty();
        }

        if (!passwordEncoder.matches(
                senha,
                usuario.get().getSenha())) {

            return Optional.empty();
        }

        return usuario;
    }
}