package br.com.solarquote.controller;

import br.com.solarquote.dto.OrcamentoRequestDTO;
import br.com.solarquote.dto.OrcamentoResponseDTO;
import br.com.solarquote.entity.Orcamento;
import br.com.solarquote.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @GetMapping
    public ResponseEntity<List<OrcamentoResponseDTO>> listarTodos() {

        List<OrcamentoResponseDTO> orcamentos =
                orcamentoService.listarTodos()
                        .stream()
                        .map(this::converterParaResponse)
                        .toList();

        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return orcamentoService.buscarPorId(id)
                .map(this::converterParaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> salvar(
            @Valid @RequestBody OrcamentoRequestDTO dados) {

        Orcamento orcamento = converterParaEntity(dados);

        return orcamentoService.salvar(
                        orcamento,
                        dados.getUsuarioId(),
                        dados.getClienteId(),
                        dados.getPainelId()
                )
                .map(this::converterParaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrcamentoRequestDTO dados) {

        Orcamento orcamento = converterParaEntity(dados);

        return orcamentoService.atualizar(
                        id,
                        orcamento,
                        dados.getUsuarioId(),
                        dados.getClienteId(),
                        dados.getPainelId()
                )
                .map(this::converterParaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!orcamentoService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    private Orcamento converterParaEntity(OrcamentoRequestDTO dados) {

        Orcamento orcamento = new Orcamento();

        orcamento.setQuantidadePaineis(dados.getQuantidadePaineis());
        orcamento.setPotenciaKwp(dados.getPotenciaKwp());
        orcamento.setGeracaoKwh(dados.getGeracaoKwh());
        orcamento.setPrecoEntrada(dados.getPrecoEntrada());
        orcamento.setPrecoInstalacao(dados.getPrecoInstalacao());
        orcamento.setValorTotal(dados.getValorTotal());
        orcamento.setPdf(dados.getPdf());

        return orcamento;
    }

    private OrcamentoResponseDTO converterParaResponse(Orcamento orcamento) {

        return new OrcamentoResponseDTO(
                orcamento.getId(),

                orcamento.getUsuario().getId(),
                orcamento.getUsuario().getNome(),

                orcamento.getCliente().getId(),
                orcamento.getCliente().getNome(),

                orcamento.getPainel().getId(),
                orcamento.getPainel().getModelo(),

                orcamento.getQuantidadePaineis(),
                orcamento.getPotenciaKwp(),
                orcamento.getGeracaoKwh(),

                orcamento.getPrecoEntrada(),
                orcamento.getPrecoInstalacao(),
                orcamento.getValorTotal(),

                orcamento.getPdf(),
                orcamento.getDataCriacao()
        );
    }
}