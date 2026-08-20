package br.com.solarquote.controller;

import br.com.solarquote.dto.UsuarioRequestDTO;
import br.com.solarquote.dto.UsuarioResponseDTO;
import br.com.solarquote.entity.Usuario;
import br.com.solarquote.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {

        List<UsuarioResponseDTO> usuarios =
                usuarioService.listarTodos()
                        .stream()
                        .map(usuario -> new UsuarioResponseDTO(
                                usuario.getId(),
                                usuario.getNome(),
                                usuario.getEmail()
                        ))
                        .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return usuarioService.buscarPorId(id)
                .map(usuario -> new UsuarioResponseDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @Valid @RequestBody UsuarioRequestDTO dados) {

        Usuario usuario = new Usuario();

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(dados.getSenha());

        Usuario salvo = usuarioService.salvar(usuario);

        UsuarioResponseDTO resposta = new UsuarioResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getEmail()
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO dados) {

        Usuario usuario = new Usuario();

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenha(dados.getSenha());

        return usuarioService.atualizar(id, usuario)
                .map(usuarioAtualizado ->
                        new UsuarioResponseDTO(
                                usuarioAtualizado.getId(),
                                usuarioAtualizado.getNome(),
                                usuarioAtualizado.getEmail()
                        )
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!usuarioService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}