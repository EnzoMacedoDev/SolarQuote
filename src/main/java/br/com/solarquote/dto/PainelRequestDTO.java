package br.com.solarquote.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PainelRequestDTO {

    @NotBlank(message = "O modelo é obrigatório")
    @Size(max = 100, message = "O modelo deve ter no máximo 100 caracteres")
    private String modelo;

    @NotBlank(message = "O fabricante é obrigatório")
    @Size(max = 100, message = "O fabricante deve ter no máximo 100 caracteres")
    private String fabricante;

    @NotNull(message = "A potência é obrigatória")
    @DecimalMin(value = "0.01", message = "A potência deve ser maior que zero")
    private BigDecimal potenciaWp;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    private Boolean ativo = true;

    public PainelRequestDTO() {
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public BigDecimal getPotenciaWp() {
        return potenciaWp;
    }

    public void setPotenciaWp(BigDecimal potenciaWp) {
        this.potenciaWp = potenciaWp;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
