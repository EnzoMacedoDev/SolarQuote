package br.com.solarquote.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orcamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_cliente", nullable = false, length = 150)
    private String nomeCliente;

    @Column(name = "local_cliente", nullable = false, length = 150)
    private String localCliente;

    @ManyToOne
    @JoinColumn(name = "painel_id", nullable = false)
    private PainelEntity painel;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "quantidade_paineis", nullable = false)
    private Integer quantidadePaineis;

    @Column(name = "potencia_kwp", nullable = false, precision = 10, scale = 3)
    private BigDecimal potenciaKwp;

    @Column(name = "geracao_kwh", nullable = false, precision = 10, scale = 2)
    private BigDecimal geracaoKwh;

    @Column(name = "preco_entrada", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoEntrada;

    @Column(name = "preco_instalacao", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoInstalacao;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "pdf")
    private byte[] pdf;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrcamentoEntity> itens = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }
}