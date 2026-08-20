package br.com.solarquote.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrcamentoResponseDTO {

    private Long id;

    private Long usuarioId;
    private String usuarioNome;

    private Long clienteId;
    private String clienteNome;

    private Long painelId;
    private String painelModelo;

    private Integer quantidadePaineis;
    private BigDecimal potenciaKwp;
    private BigDecimal geracaoKwh;

    private BigDecimal precoEntrada;
    private BigDecimal precoInstalacao;
    private BigDecimal valorTotal;

    private String pdf;

    private LocalDateTime dataCriacao;

    public OrcamentoResponseDTO() {
    }

    public OrcamentoResponseDTO(
            Long id,
            Long usuarioId,
            String usuarioNome,
            Long clienteId,
            String clienteNome,
            Long painelId,
            String painelModelo,
            Integer quantidadePaineis,
            BigDecimal potenciaKwp,
            BigDecimal geracaoKwh,
            BigDecimal precoEntrada,
            BigDecimal precoInstalacao,
            BigDecimal valorTotal,
            String pdf,
            LocalDateTime dataCriacao) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.painelId = painelId;
        this.painelModelo = painelModelo;
        this.quantidadePaineis = quantidadePaineis;
        this.potenciaKwp = potenciaKwp;
        this.geracaoKwh = geracaoKwh;
        this.precoEntrada = precoEntrada;
        this.precoInstalacao = precoInstalacao;
        this.valorTotal = valorTotal;
        this.pdf = pdf;
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public Long getPainelId() {
        return painelId;
    }

    public String getPainelModelo() {
        return painelModelo;
    }

    public Integer getQuantidadePaineis() {
        return quantidadePaineis;
    }

    public BigDecimal getPotenciaKwp() {
        return potenciaKwp;
    }

    public BigDecimal getGeracaoKwh() {
        return geracaoKwh;
    }

    public BigDecimal getPrecoEntrada() {
        return precoEntrada;
    }

    public BigDecimal getPrecoInstalacao() {
        return precoInstalacao;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getPdf() {
        return pdf;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}