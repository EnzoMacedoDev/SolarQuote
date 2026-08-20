package br.com.solarquote.service;

import br.com.solarquote.entity.Usuario;
import br.com.solarquote.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {

        usuario.setSenha(
                passwordEncoder.encode(usuario.getSenha())
        );

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> atualizar(Long id, Usuario dados) {

        return usuarioRepository.findById(id)
                .map(usuario -> {

                    usuario.setNome(dados.getNome());
                    usuario.setEmail(dados.getEmail());

                    if (dados.getSenha() != null &&
                            !dados.getSenha().isBlank()) {

                        usuario.setSenha(
                                passwordEncoder.encode(dados.getSenha())
                        );
                    }

                    return usuarioRepository.save(usuario);
                });
    }

    public boolean excluir(Long id) {

        if (!usuarioRepository.existsById(id)) {
            return false;
        }

        usuarioRepository.deleteById(id);

        return true;
    }
}