package br.com.solarquote.dto;

import br.com.solarquote.entity.OrcamentoEntity;
import br.com.solarquote.entity.PainelEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrcamentoResponse(
        Long id,
        String nomeCliente,
        String localCliente,
        PainelEntity painel,
        String usuario,
        Integer quantidadePaineis,
        BigDecimal potenciaKwp,
        BigDecimal geracaoKwh,
        BigDecimal precoEntrada,
        BigDecimal precoInstalacao,
        BigDecimal valorTotal,
        List<ItemOrcamentoResponse> itens,
        LocalDateTime dataCriacao
) {
    public static OrcamentoResponse from(OrcamentoEntity o) {
        return new OrcamentoResponse(
                o.getId(),
                o.getNomeCliente(),
                o.getLocalCliente(),
                o.getPainel(),
                o.getUsuario().getUsername(),
                o.getQuantidadePaineis(),
                o.getPotenciaKwp(),
                o.getGeracaoKwh(),
                o.getPrecoEntrada(),
                o.getPrecoInstalacao(),
                o.getValorTotal(),
                o.getItens().stream().map(ItemOrcamentoResponse::from).toList(),
                o.getDataCriacao()
        );
    }
}