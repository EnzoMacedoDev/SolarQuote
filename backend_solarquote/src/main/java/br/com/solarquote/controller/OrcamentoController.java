package br.com.solarquote.controller;

import br.com.solarquote.dto.OrcamentoRequest;
import br.com.solarquote.dto.OrcamentoResponse;
import br.com.solarquote.service.OrcamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @GetMapping
    public List<OrcamentoResponse> listar() {
        return orcamentoService.listar();
    }

    @GetMapping("/{id}")
    public OrcamentoResponse buscarPorId(@PathVariable Long id) {
        return orcamentoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrcamentoResponse criar(@Valid @RequestBody OrcamentoRequest request) {
        return orcamentoService.criar(request);
    }

    @PutMapping("/{id}")
    public OrcamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody OrcamentoRequest request) {
        return orcamentoService.atualizar(id, request);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> baixarPdf(@PathVariable Long id) {
        byte[] pdf = orcamentoService.buscarPdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=orcamento-" + id + ".pdf")
                .body(pdf);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        orcamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}