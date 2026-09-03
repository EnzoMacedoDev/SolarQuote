package br.com.solarquote.dto;

import jakarta.validation.constraints.NotBlank;

public record ItemOrcamentoRequest(
        @NotBlank String descricao,
        @NotBlank String quantidade
) {}