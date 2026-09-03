package br.com.solarquote.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoRequest(
        @NotBlank String nomeCliente,
        @NotBlank String localCliente,
        @NotNull Long painelId,
        @NotNull @Positive Integer quantidadePaineis,
        @NotNull @PositiveOrZero BigDecimal precoEntrada,
        @NotNull @PositiveOrZero BigDecimal precoInstalacao,
        @Valid List<ItemOrcamentoRequest> itens
) {}