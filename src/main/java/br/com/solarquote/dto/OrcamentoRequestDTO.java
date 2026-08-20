package br.com.solarquote.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrcamentoRequestDTO {

    @NotNull(message = "O usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O painel é obrigatório")
    private Long painelId;

    @NotNull(message = "A quantidade de painéis é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidadePaineis;

    @NotNull(message = "A potência é obrigatória")
    @DecimalMin(value = "0.01", message = "A potência deve ser maior que zero")
    private BigDecimal potenciaKwp;

    @NotNull(message = "A geração é obrigatória")
    @DecimalMin(value = "0.01", message = "A geração deve ser maior que zero")
    private BigDecimal geracaoKwh;

    @NotNull(message = "O preço de entrada é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    private BigDecimal precoEntrada;

    @NotNull(message = "O preço de instalação é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    private BigDecimal precoInstalacao;

    @NotNull(message = "O valor total é obrigatório")
    @DecimalMin(value = "0.00", message = "O valor não pode ser negativo")
    private BigDecimal valorTotal;

    private String pdf;

    public OrcamentoRequestDTO() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getPainelId() {
        return painelId;
    }

    public void setPainelId(Long painelId) {
        this.painelId = painelId;
    }

    public Integer getQuantidadePaineis() {
        return quantidadePaineis;
    }

    public void setQuantidadePaineis(Integer quantidadePaineis) {
        this.quantidadePaineis = quantidadePaineis;
    }

    public BigDecimal getPotenciaKwp() {
        return potenciaKwp;
    }

    public void setPotenciaKwp(BigDecimal potenciaKwp) {
        this.potenciaKwp = potenciaKwp;
    }

    public BigDecimal getGeracaoKwh() {
        return geracaoKwh;
    }

    public void setGeracaoKwh(BigDecimal geracaoKwh) {
        this.geracaoKwh = geracaoKwh;
    }

    public BigDecimal getPrecoEntrada() {
        return precoEntrada;
    }

    public void setPrecoEntrada(BigDecimal precoEntrada) {
        this.precoEntrada = precoEntrada;
    }

    public BigDecimal getPrecoInstalacao() {
        return precoInstalacao;
    }

    public void setPrecoInstalacao(BigDecimal precoInstalacao) {
        this.precoInstalacao = precoInstalacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}