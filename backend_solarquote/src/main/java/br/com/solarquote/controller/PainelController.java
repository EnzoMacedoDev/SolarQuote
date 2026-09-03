package br.com.solarquote.controller;


import br.com.solarquote.entity.PainelEntity;
import br.com.solarquote.service.PainelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/paineis")
@RequiredArgsConstructor
public class PainelController {

    private final PainelService painelService;

    @GetMapping
    public List<PainelEntity> listar() {
        return painelService.listar();
    }

    @GetMapping("/{id}")
    public PainelEntity buscarPorId(@PathVariable Long id) {
        return painelService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PainelEntity criar(@Valid @RequestBody PainelEntity painel) {
        return painelService.criar(painel);
    }

    @PutMapping("/{id}")
    public PainelEntity atualizar(@PathVariable Long id, @Valid @RequestBody PainelEntity painel) {
        return painelService.atualizar(id, painel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        painelService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}