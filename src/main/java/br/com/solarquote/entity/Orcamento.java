package br.com.solarquote.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "painel_id", nullable = false)
    private Painel painel;

    @Column(name = "quantidade_paineis", nullable = false)
    private Integer quantidadePaineis;

    @Column(name = "potencia_kwp", nullable = false, precision = 10, scale = 2)
    private BigDecimal potenciaKwp;

    @Column(name = "geracao_kwh", nullable = false, precision = 10, scale = 2)
    private BigDecimal geracaoKwh;

    @Column(name = "preco_entrada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoEntrada;

    @Column(name = "preco_instalacao", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoInstalacao;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(length = 255)
    private String pdf;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public Orcamento() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Painel getPainel() {
        return painel;
    }

    public void setPainel(Painel painel) {
        this.painel = painel;
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

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}