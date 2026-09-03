package br.com.solarquote.dto;

import br.com.solarquote.entity.ItemOrcamentoEntity;

public record ItemOrcamentoResponse(
        Long id,
        String descricao,
        String quantidade
) {
    public static ItemOrcamentoResponse from(ItemOrcamentoEntity item) {
        return new ItemOrcamentoResponse(item.getId(), item.getDescricao(), item.getQuantidade());
    }
}