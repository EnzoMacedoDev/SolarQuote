package br.com.solarquote.controller;

import br.com.solarquote.dto.ClienteRequestDTO;
import br.com.solarquote.dto.ClienteResponseDTO;
import br.com.solarquote.entity.Cliente;
import br.com.solarquote.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {

        List<ClienteResponseDTO> clientes =
                clienteService.listarTodos()
                        .stream()
                        .map(cliente -> new ClienteResponseDTO(
                                cliente.getId(),
                                cliente.getNome(),
                                cliente.getLocal()
                        ))
                        .toList();

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return clienteService.buscarPorId(id)
                .map(cliente -> new ClienteResponseDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getLocal()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(
            @Valid @RequestBody ClienteRequestDTO dados) {

        Cliente cliente = new Cliente();

        cliente.setNome(dados.getNome());
        cliente.setLocal(dados.getLocal());

        Cliente salvo = clienteService.salvar(cliente);

        ClienteResponseDTO resposta = new ClienteResponseDTO(
                salvo.getId(),
                salvo.getNome(),
                salvo.getLocal()
        );

        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dados) {

        Cliente cliente = new Cliente();

        cliente.setNome(dados.getNome());
        cliente.setLocal(dados.getLocal());

        return clienteService.atualizar(id, cliente)
                .map(clienteAtualizado ->
                        new ClienteResponseDTO(
                                clienteAtualizado.getId(),
                                clienteAtualizado.getNome(),
                                clienteAtualizado.getLocal()
                        )
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!clienteService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}