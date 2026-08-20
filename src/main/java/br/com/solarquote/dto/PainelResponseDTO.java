package br.com.solarquote.dto;

import java.math.BigDecimal;

public class PainelResponseDTO {

    private Long id;
    private String modelo;
    private String fabricante;
    private BigDecimal potenciaWp;
    private BigDecimal preco;
    private Boolean ativo;

    public PainelResponseDTO() {
    }

    public PainelResponseDTO(
            Long id,
            String modelo,
            String fabricante,
            BigDecimal potenciaWp,
            BigDecimal preco,
            Boolean ativo) {

        this.id = id;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.potenciaWp = potenciaWp;
        this.preco = preco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public BigDecimal getPotenciaWp() {
        return potenciaWp;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}