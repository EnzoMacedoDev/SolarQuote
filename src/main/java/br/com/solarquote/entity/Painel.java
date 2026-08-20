package br.com.solarquote.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "paineis")
public class Painel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 100)
    private String fabricante;

    @Column(name = "potencia_wp", nullable = false, precision = 10, scale = 2)
    private BigDecimal potenciaWp;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Painel() {
    }

    public Painel(String modelo, String fabricante, BigDecimal potenciaWp, BigDecimal preco) {
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.potenciaWp = potenciaWp;
        this.preco = preco;
        this.ativo = true;
    }

    public Long getId() {
        return id;
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