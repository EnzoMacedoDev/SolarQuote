package br.com.solarquote.controller;

import br.com.solarquote.dto.PainelRequestDTO;
import br.com.solarquote.dto.PainelResponseDTO;
import br.com.solarquote.entity.Painel;
import br.com.solarquote.service.PainelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paineis")
public class PainelController {

    private final PainelService painelService;

    public PainelController(PainelService painelService) {
        this.painelService = painelService;
    }

    @GetMapping
    public ResponseEntity<List<PainelResponseDTO>> listarTodos() {

        List<PainelResponseDTO> paineis =
                painelService.listarTodos()
                        .stream()
                        .map(this::converterParaResponse)
                        .toList();

        return ResponseEntity.ok(paineis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PainelResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return painelService.buscarPorId(id)
                .map(this::converterParaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PainelResponseDTO> salvar(
            @Valid @RequestBody PainelRequestDTO dados) {

        Painel painel = new Painel();

        painel.setModelo(dados.getModelo());
        painel.setFabricante(dados.getFabricante());
        painel.setPotenciaWp(dados.getPotenciaWp());
        painel.setPreco(dados.getPreco());
        painel.setAtivo(dados.getAtivo());

        Painel salvo = painelService.salvar(painel);

        return ResponseEntity.ok(converterParaResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PainelResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PainelRequestDTO dados) {

        Painel painel = new Painel();

        painel.setModelo(dados.getModelo());
        painel.setFabricante(dados.getFabricante());
        painel.setPotenciaWp(dados.getPotenciaWp());
        painel.setPreco(dados.getPreco());
        painel.setAtivo(dados.getAtivo());

        return painelService.atualizar(id, painel)
                .map(this::converterParaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!painelService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private PainelResponseDTO converterParaResponse(Painel painel) {

        return new PainelResponseDTO(
                painel.getId(),
                painel.getModelo(),
                painel.getFabricante(),
                painel.getPotenciaWp(),
                painel.getPreco(),
                painel.getAtivo()
        );
    }
}