package br.com.solarquote.service;

import br.com.solarquote.entity.ItemOrcamentoEntity;
import br.com.solarquote.entity.OrcamentoEntity;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.ColumnText;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPCellEvent;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfPageEventHelper;
import org.openpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

@Service
public class PdfService {

    private static final Locale LOCALE_BR = new Locale("pt", "BR");


    /*
     * =====================================================
     * ARQUIVOS DE IMAGEM
     * =====================================================
     */

    private static final String CAMINHO_LOGO = "/img/logo-gds.png";
    private static final String CAMINHO_FOTO = "/img/foto-instalacao.jpg";


    /*
     * =====================================================
     * DIMENSÕES
     * =====================================================
     */

    private static final float LARGURA_PAGINA = PageSize.A4.getWidth();
    private static final float ALTURA_PAGINA = PageSize.A4.getHeight();

    private static final float MARGEM_LATERAL = 40f;
    private static final float ALTURA_CABECALHO = 155f;
    private static final float ALTURA_RODAPE = 42f;


    /*
     * =====================================================
     * PALETA GDS SOLAR
     * =====================================================
     */

    private static final Color VERDE_PRINCIPAL =
            new Color(24, 104, 62);

    private static final Color VERDE_ESCURO =
            new Color(12, 64, 38);

    private static final Color VERDE_MEDIO =
            new Color(42, 145, 82);

    private static final Color VERDE_CLARO =
            new Color(220, 242, 226);

    private static final Color VERDE_DESTAQUE =
            new Color(111, 196, 92);

    private static final Color FUNDO_SUAVE =
            new Color(247, 250, 248);

    private static final Color CINZA_TEXTO =
            new Color(55, 65, 60);

    private static final Color CINZA_CLARO =
            new Color(225, 232, 227);

    private static final Color CINZA_BORDA =
            new Color(205, 215, 208);

    private static final Color BRANCO =
            Color.WHITE;

    private static final Color PRETO =
            new Color(25, 30, 27);


    /*
     * =====================================================
     * FONTES
     * =====================================================
     */

    private static final Font FONTE_TITULO_HEADER =
            new Font(Font.HELVETICA, 23, Font.BOLD, BRANCO);

    private static final Font FONTE_SUBTITULO_HEADER =
            new Font(Font.HELVETICA, 10, Font.NORMAL, VERDE_CLARO);

    private static final Font FONTE_ROTULO_CARD =
            new Font(Font.HELVETICA, 9, Font.NORMAL, CINZA_TEXTO);

    private static final Font FONTE_VALOR_CARD =
            new Font(Font.HELVETICA, 17, Font.BOLD, VERDE_ESCURO);

    private static final Font FONTE_DADOS_CLIENTE_ROTULO =
            new Font(Font.HELVETICA, 9, Font.BOLD, VERDE_PRINCIPAL);

    private static final Font FONTE_DADOS_CLIENTE_VALOR =
            new Font(Font.HELVETICA, 10.5f, Font.NORMAL, PRETO);

    private static final Font FONTE_TITULO_SECAO =
            new Font(Font.HELVETICA, 20, Font.BOLD, VERDE_ESCURO);

    private static final Font FONTE_SUBTITULO_SECAO =
            new Font(Font.HELVETICA, 10, Font.NORMAL, VERDE_MEDIO);

    private static final Font FONTE_TABELA_CABECALHO =
            new Font(Font.HELVETICA, 9.5f, Font.BOLD, BRANCO);

    private static final Font FONTE_TABELA =
            new Font(Font.HELVETICA, 9.5f, Font.NORMAL, CINZA_TEXTO);

    private static final Font FONTE_TABELA_QTD =
            new Font(Font.HELVETICA, 10, Font.BOLD, VERDE_ESCURO);

    private static final Font FONTE_TITULO_PAGAMENTO =
            new Font(Font.HELVETICA, 27, Font.BOLD, VERDE_ESCURO);

    private static final Font FONTE_SUBTITULO_PAGAMENTO =
            new Font(Font.HELVETICA, 10, Font.NORMAL, CINZA_TEXTO);

    private static final Font FONTE_ROTULO_PAGAMENTO =
            new Font(Font.HELVETICA, 10, Font.NORMAL, CINZA_TEXTO);

    private static final Font FONTE_VALOR_PAGAMENTO =
            new Font(Font.HELVETICA, 18, Font.BOLD, VERDE_ESCURO);

    private static final Font FONTE_RODAPE =
            new Font(Font.HELVETICA, 8, Font.NORMAL, BRANCO);


    /*
     * =====================================================
     * GERAÇÃO DO PDF
     * =====================================================
     */

    public byte[] gerarPdfOrcamento(OrcamentoEntity orcamento) {

        Document documento = new Document(
                PageSize.A4,
                MARGEM_LATERAL,
                MARGEM_LATERAL,
                ALTURA_CABECALHO + 15,
                ALTURA_RODAPE + 18
        );

        try {

            ByteArrayOutputStream saida =
                    new ByteArrayOutputStream();

            PdfWriter writer =
                    PdfWriter.getInstance(documento, saida);

            writer.setPageEvent(new RodapeEvento());

            documento.open();


            /*
             * CABEÇALHO
             */

            desenharCabecalhoTopo(writer);


            adicionarEspaco(documento, 12);


            /*
             * CARDS DE DESTAQUE
             */

            documento.add(
                    criarCardsResumo(orcamento)
            );


            adicionarEspaco(documento, 18);


            /*
             * DADOS DO CLIENTE
             */

            adicionarDadosCliente(
                    documento,
                    orcamento
            );


            adicionarEspaco(documento, 22);


            /*
             * EQUIPAMENTOS
             */

            adicionarTituloSecao(
                    documento,
                    "EQUIPAMENTOS",
                    "Componentes do Gerador Fotovoltaico"
            );


            adicionarEspaco(documento, 12);


            if (orcamento.getItens() != null
                    && !orcamento.getItens().isEmpty()) {

                documento.add(
                        criarTabelaItens(
                                orcamento.getItens()
                        )
                );
            }


            /*
             * NOVA PÁGINA
             */

            documento.newPage();


            /*
             * PAGAMENTO
             */

            adicionarPaginaPagamento(
                    documento,
                    orcamento
            );


            documento.close();

            return saida.toByteArray();


        } catch (DocumentException e) {

            throw new RuntimeException(
                    "Erro ao gerar PDF do orçamento",
                    e
            );

        }
    }


    /*
     * =====================================================
     * CABEÇALHO PREMIUM
     * =====================================================
     */

    private void desenharCabecalhoTopo(
            PdfWriter writer
    ) throws DocumentException {

        PdfContentByte cb =
                writer.getDirectContent();

        float baseCabecalho =
                ALTURA_PAGINA - ALTURA_CABECALHO;


        /*
         * FUNDO EM DEGRADÊ
         */

        desenharGradienteHorizontal(
                cb,
                0,
                baseCabecalho,
                LARGURA_PAGINA,
                ALTURA_CABECALHO,
                VERDE_ESCURO,
                VERDE_MEDIO
        );


        /*
         * DETALHE DECORATIVO
         */

        cb.saveState();

        cb.setColorFill(
                new Color(255, 255, 255, 18)
        );

        cb.circle(
                LARGURA_PAGINA - 45,
                ALTURA_PAGINA - 35,
                120
        );

        cb.fill();

        cb.restoreState();


        /*
         * LOGO
         */

        float larguraLogo = 88f;

        try {

            Image logo =
                    Image.getInstance(
                            carregarRecurso(CAMINHO_LOGO)
                    );

            logo.scaleToFit(
                    larguraLogo,
                    larguraLogo
            );

            float logoX =
                    MARGEM_LATERAL;

            float logoY =
                    baseCabecalho
                            + (ALTURA_CABECALHO
                            - logo.getScaledHeight()) / 2;

            logo.setAbsolutePosition(
                    logoX,
                    logoY
            );

            cb.addImage(logo);

        } catch (Exception e) {

            // Cabeçalho continua sem logo caso não encontrada.

        }


        /*
         * FOTO
         */

        float larguraFoto = 135f;
        float alturaFoto = 92f;

        try {

            Image foto =
                    Image.getInstance(
                            carregarRecurso(CAMINHO_FOTO)
                    );

            foto.scaleToFit(
                    larguraFoto,
                    alturaFoto
            );


            float fotoX =
                    LARGURA_PAGINA
                            - MARGEM_LATERAL
                            - foto.getScaledWidth();

            float fotoY =
                    baseCabecalho
                            + (ALTURA_CABECALHO
                            - foto.getScaledHeight()) / 2;


            foto.setAbsolutePosition(
                    fotoX,
                    fotoY
            );

            cb.addImage(foto);


        } catch (Exception e) {

            // Cabeçalho continua sem foto.

        }


        /*
         * TEXTO CENTRAL
         */

        float inicioTextoX =
                MARGEM_LATERAL
                        + larguraLogo
                        + 22;

        float fimTextoX =
                LARGURA_PAGINA
                        - MARGEM_LATERAL
                        - larguraFoto
                        - 20;


        ColumnText coluna =
                new ColumnText(cb);


        coluna.setSimpleColumn(
                inicioTextoX,
                baseCabecalho + 28,
                fimTextoX,
                ALTURA_PAGINA - 25
        );


        coluna.setLeading(27f);

        coluna.addText(
                new Phrase(
                        "ENERGIA QUE TRANSFORMA",
                        FONTE_TITULO_HEADER
                )
        );

        coluna.addText(
                new Phrase(
                        "\nSoluções inteligentes em energia solar",
                        FONTE_SUBTITULO_HEADER
                )
        );

        coluna.go();
    }


    /*
     * =====================================================
     * CARDS DE RESUMO
     * =====================================================
     */

    private PdfPTable criarCardsResumo(
            OrcamentoEntity orcamento
    ) throws DocumentException {

        PdfPTable tabela =
                new PdfPTable(2);

        tabela.setWidthPercentage(100);

        tabela.setWidths(
                new float[]{1f, 1f}
        );


        /*
         * CARD GERAÇÃO
         */

        PdfPCell cardGeracao =
                criarCardResumo(
                        "GERAÇÃO ESTIMADA",
                        formatarKwh(
                                orcamento.getGeracaoKwh()
                        ) + " kWh"
                );

        tabela.addCell(cardGeracao);


        /*
         * ESPAÇAMENTO
         */

        PdfPCell espaco =
                new PdfPCell();

        espaco.setBorder(
                Rectangle.NO_BORDER
        );

        espaco.setFixedHeight(10);

        tabela.addCell(espaco);


        /*
         * CARD GERADOR
         */

        PdfPCell cardGerador =
                criarCardResumo(
                        "POTÊNCIA DO GERADOR",
                        formatarKwp(
                                orcamento.getPotenciaKwp()
                        ) + " kWp"
                );

        tabela.addCell(cardGerador);


        PdfPCell espacoFinal =
                new PdfPCell();

        espacoFinal.setBorder(
                Rectangle.NO_BORDER
        );

        tabela.addCell(espacoFinal);


        return tabela;
    }


    private PdfPCell criarCardResumo(
            String titulo,
            String valor
    ) {

        Paragraph rotulo =
                new Paragraph(
                        titulo,
                        FONTE_ROTULO_CARD
                );

        rotulo.setSpacingAfter(5f);


        Paragraph valorParagraph =
                new Paragraph(
                        valor,
                        FONTE_VALOR_CARD
                );


        PdfPCell celula =
                new PdfPCell();

        celula.setBorder(
                Rectangle.NO_BORDER
        );

        celula.setPaddingTop(13f);
        celula.setPaddingBottom(13f);
        celula.setPaddingLeft(16f);
        celula.setPaddingRight(16f);

        celula.setCellEvent(
                new CaixaArredondadaEvento(
                        VERDE_CLARO,
                        10f
                )
        );

        celula.addElement(rotulo);

        celula.addElement(
                valorParagraph
        );


        return celula;
    }


    /*
     * =====================================================
     * DADOS DO CLIENTE
     * =====================================================
     */

    private void adicionarDadosCliente(
            Document documento,
            OrcamentoEntity orcamento
    ) throws DocumentException {


        String dataFormatada =
                orcamento.getDataCriacao() != null
                        ? orcamento.getDataCriacao().format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy"
                        )
                )
                        : "";


        PdfPTable tabela =
                new PdfPTable(1);

        tabela.setWidthPercentage(100);


        PdfPCell celula =
                new PdfPCell();

        celula.setBorder(
                Rectangle.NO_BORDER
        );

        celula.setPadding(16f);

        celula.setCellEvent(
                new CaixaArredondadaEvento(
                        FUNDO_SUAVE,
                        8f
                )
        );


        Paragraph titulo =
                new Paragraph(
                        "DADOS DO CLIENTE",
                        FONTE_DADOS_CLIENTE_ROTULO
                );

        titulo.setSpacingAfter(10f);


        celula.addElement(titulo);


        celula.addElement(
                criarLinhaCliente(
                        "Cliente",
                        valorOuVazio(
                                orcamento.getNomeCliente()
                        )
                )
        );


        celula.addElement(
                criarLinhaCliente(
                        "Local",
                        valorOuVazio(
                                orcamento.getLocalCliente()
                        )
                )
        );


        celula.addElement(
                criarLinhaCliente(
                        "Data",
                        dataFormatada
                )
        );


        tabela.addCell(celula);

        documento.add(tabela);
    }


    private Paragraph criarLinhaCliente(
            String rotulo,
            String valor
    ) {

        Paragraph linha =
                new Paragraph();

        linha.add(
                new Chunk(
                        rotulo + ": ",
                        FONTE_DADOS_CLIENTE_ROTULO
                )
        );

        linha.add(
                new Chunk(
                        valor,
                        FONTE_DADOS_CLIENTE_VALOR
                )
        );

        linha.setSpacingAfter(5f);

        return linha;
    }


    private String valorOuVazio(
            String texto
    ) {

        return texto != null
                ? texto
                : "";
    }


    /*
     * =====================================================
     * TÍTULO DE SEÇÃO
     * =====================================================
     */

    private void adicionarTituloSecao(
            Document documento,
            String titulo,
            String subtitulo
    ) throws DocumentException {


        Paragraph tituloParagraph =
                new Paragraph(
                        titulo,
                        FONTE_TITULO_SECAO
                );

        tituloParagraph.setAlignment(
                Element.ALIGN_CENTER
        );

        tituloParagraph.setSpacingAfter(3f);


        documento.add(
                tituloParagraph
        );


        Paragraph subtituloParagraph =
                new Paragraph(
                        subtitulo,
                        FONTE_SUBTITULO_SECAO
                );

        subtituloParagraph.setAlignment(
                Element.ALIGN_CENTER
        );

        documento.add(
                subtituloParagraph
        );


        adicionarEspaco(
                documento,
                3f
        );
    }


    /*
     * =====================================================
     * TABELA DE EQUIPAMENTOS
     * =====================================================
     */

    private PdfPTable criarTabelaItens(
            List<ItemOrcamentoEntity> itens
    ) throws DocumentException {


        PdfPTable tabela =
                new PdfPTable(2);

        tabela.setWidthPercentage(100);

        tabela.setWidths(
                new float[]{3.5f, 1f}
        );


        /*
         * CABEÇALHO - ITEM
         */

        PdfPCell cabecalhoItem =
                new PdfPCell(
                        new Phrase(
                                "DESCRIÇÃO DO EQUIPAMENTO",
                                FONTE_TABELA_CABECALHO
                        )
                );

        configurarCabecalhoTabela(
                cabecalhoItem,
                Element.ALIGN_LEFT
        );

        tabela.addCell(
                cabecalhoItem
        );


        /*
         * CABEÇALHO - QUANTIDADE
         */

        PdfPCell cabecalhoQuantidade =
                new PdfPCell(
                        new Phrase(
                                "QTD.",
                                FONTE_TABELA_CABECALHO
                        )
                );

        configurarCabecalhoTabela(
                cabecalhoQuantidade,
                Element.ALIGN_CENTER
        );

        tabela.addCell(
                cabecalhoQuantidade
        );


        /*
         * ITENS
         */

        boolean linhaAlternada = false;


        for (ItemOrcamentoEntity item : itens) {


            Color fundo =
                    linhaAlternada
                            ? FUNDO_SUAVE
                            : BRANCO;


            /*
             * DESCRIÇÃO
             */

            PdfPCell descricao =
                    new PdfPCell(
                            new Phrase(
                                    item.getDescricao(),
                                    FONTE_TABELA
                            )
                    );

            configurarCelulaTabela(
                    descricao,
                    fundo,
                    Element.ALIGN_LEFT
            );

            tabela.addCell(
                    descricao
            );


            /*
             * QUANTIDADE
             */

            PdfPCell quantidade =
                    new PdfPCell(
                            new Phrase(
                                    item.getQuantidade(),
                                    FONTE_TABELA_QTD
                            )
                    );

            configurarCelulaTabela(
                    quantidade,
                    fundo,
                    Element.ALIGN_CENTER
            );

            tabela.addCell(
                    quantidade
            );


            linhaAlternada =
                    !linhaAlternada;
        }


        return tabela;
    }


    private void configurarCabecalhoTabela(
            PdfPCell celula,
            int alinhamento
    ) {

        celula.setBackgroundColor(
                VERDE_PRINCIPAL
        );

        celula.setPaddingTop(11f);
        celula.setPaddingBottom(11f);
        celula.setPaddingLeft(12f);
        celula.setPaddingRight(12f);

        celula.setHorizontalAlignment(
                alinhamento
        );

        celula.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celula.setBorderColor(
                VERDE_PRINCIPAL
        );
    }


    private void configurarCelulaTabela(
            PdfPCell celula,
            Color fundo,
            int alinhamento
    ) {

        celula.setBackgroundColor(
                fundo
        );

        celula.setPaddingTop(11f);
        celula.setPaddingBottom(11f);
        celula.setPaddingLeft(12f);
        celula.setPaddingRight(12f);

        celula.setHorizontalAlignment(
                alinhamento
        );

        celula.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );

        celula.setBorderColor(
                CINZA_CLARO
        );

        celula.setBorderWidth(0.7f);
    }


    /*
     * =====================================================
     * PÁGINA DE PAGAMENTO
     * =====================================================
     */

    private void adicionarPaginaPagamento(
            Document documento,
            OrcamentoEntity orcamento
    ) throws DocumentException {


        adicionarEspaco(
                documento,
                75
        );


        Paragraph titulo =
                new Paragraph(
                        "Investimento",
                        FONTE_TITULO_PAGAMENTO
                );

        titulo.setAlignment(
                Element.ALIGN_CENTER
        );

        documento.add(titulo);


        adicionarEspaco(
                documento,
                8
        );


        Paragraph subtitulo =
                new Paragraph(
                        "AS FORMAS DE PAGAMENTO SÃO NEGOCIADAS DIRETAMENTE COM O VENDEDOR",
                        FONTE_SUBTITULO_PAGAMENTO
                );

        subtitulo.setAlignment(
                Element.ALIGN_CENTER
        );

        documento.add(subtitulo);


        adicionarEspaco(
                documento,
                42
        );


        documento.add(
                criarCaixaPagamento(
                        orcamento
                )
        );


        adicionarEspaco(
                documento,
                25
        );


        Paragraph mensagem =
                new Paragraph(
                        "Invista hoje em economia, sustentabilidade e independência energética.",
                        FONTE_SUBTITULO_SECAO
                );

        mensagem.setAlignment(
                Element.ALIGN_CENTER
        );

        documento.add(mensagem);
    }


    /*
     * =====================================================
     * CAIXA DE PAGAMENTO
     * =====================================================
     */

    private PdfPTable criarCaixaPagamento(
            OrcamentoEntity orcamento
    ) throws DocumentException {


        PdfPTable tabelaInterna =
                new PdfPTable(1);

        tabelaInterna.setWidthPercentage(100);


        /*
         * ENTRADA
         */

        tabelaInterna.addCell(
                criarLinhaPagamento(
                        "ENTRADA",
                        formatarMoeda(
                                orcamento.getPrecoEntrada()
                        )
                )
        );


        /*
         * INSTALAÇÃO
         */

        tabelaInterna.addCell(
                criarLinhaPagamento(
                        "NA INSTALAÇÃO",
                        formatarMoeda(
                                orcamento.getPrecoInstalacao()
                        )
                )
        );


        /*
         * CONTAINER INTERNO
         */

        PdfPCell celulaInterna =
                new PdfPCell();

        celulaInterna.setBorder(
                Rectangle.NO_BORDER
        );

        celulaInterna.setPadding(6f);

        celulaInterna.setCellEvent(
                new CaixaArredondadaEvento(
                        BRANCO,
                        12f
                )
        );

        celulaInterna.addElement(
                tabelaInterna
        );


        /*
         * TABELA INTERNA
         */

        PdfPTable caixaInterna =
                new PdfPTable(1);

        caixaInterna.setWidthPercentage(100);

        caixaInterna.addCell(
                celulaInterna
        );


        /*
         * CONTAINER EXTERNO VERDE
         */

        PdfPCell celulaExterna =
                new PdfPCell();

        celulaExterna.setBorder(
                Rectangle.NO_BORDER
        );

        celulaExterna.setPadding(12f);

        celulaExterna.setCellEvent(
                new CaixaArredondadaEvento(
                        VERDE_PRINCIPAL,
                        15f
                )
        );

        celulaExterna.addElement(
                caixaInterna
        );


        PdfPTable caixaExterna =
                new PdfPTable(1);

        caixaExterna.setWidthPercentage(82);

        caixaExterna.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        caixaExterna.addCell(
                celulaExterna
        );


        return caixaExterna;
    }


    private PdfPCell criarLinhaPagamento(
            String rotulo,
            String valor
    ) {


        PdfPTable tabela =
                new PdfPTable(2);

        try {

            tabela.setWidthPercentage(100);

            tabela.setWidths(
                    new float[]{1f, 1f}
            );

        } catch (DocumentException e) {

            throw new RuntimeException(e);

        }


        PdfPCell celulaRotulo =
                new PdfPCell(
                        new Phrase(
                                rotulo,
                                FONTE_ROTULO_PAGAMENTO
                        )
                );

        celulaRotulo.setBorder(
                Rectangle.NO_BORDER
        );

        celulaRotulo.setPadding(14f);

        celulaRotulo.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );


        PdfPCell celulaValor =
                new PdfPCell(
                        new Phrase(
                                valor,
                                FONTE_VALOR_PAGAMENTO
                        )
                );

        celulaValor.setBorder(
                Rectangle.NO_BORDER
        );

        celulaValor.setPadding(14f);

        celulaValor.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        celulaValor.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );


        tabela.addCell(
                celulaRotulo
        );

        tabela.addCell(
                celulaValor
        );


        PdfPCell resultado =
                new PdfPCell();

        resultado.setBorder(
                Rectangle.NO_BORDER
        );

        resultado.setBorderColorBottom(
                CINZA_CLARO
        );

        resultado.setBorderWidthBottom(
                1f
        );

        resultado.addElement(
                tabela
        );


        return resultado;
    }


    /*
     * =====================================================
     * EVENTO DE CAIXA ARREDONDADA
     * =====================================================
     */

    private static class CaixaArredondadaEvento
            implements PdfPCellEvent {


        private final Color cor;

        private final float raio;


        CaixaArredondadaEvento(
                Color cor,
                float raio
        ) {

            this.cor = cor;
            this.raio = raio;

        }


        @Override
        public void cellLayout(
                PdfPCell cell,
                Rectangle posicao,
                PdfContentByte[] canvases
        ) {


            PdfContentByte cb =
                    canvases[
                            PdfPTable.BACKGROUNDCANVAS
                            ];


            cb.saveState();

            cb.setColorFill(cor);

            cb.roundRectangle(
                    posicao.getLeft(),
                    posicao.getBottom(),
                    posicao.getWidth(),
                    posicao.getHeight(),
                    raio
            );

            cb.fill();

            cb.restoreState();
        }
    }


    /*
     * =====================================================
     * RODAPÉ
     * =====================================================
     */

    private class RodapeEvento
            extends PdfPageEventHelper {


        @Override
        public void onEndPage(
                PdfWriter writer,
                Document document
        ) {


            PdfContentByte cb =
                    writer.getDirectContent();


            /*
             * FUNDO
             */

            desenharGradienteHorizontal(
                    cb,
                    0,
                    0,
                    LARGURA_PAGINA,
                    ALTURA_RODAPE,
                    VERDE_ESCURO,
                    VERDE_PRINCIPAL
            );


            /*
             * TEXTO
             */

            ColumnText.showTextAligned(
                    cb,
                    Element.ALIGN_LEFT,
                    new Phrase(
                            "GDS ENERGIA SOLAR",
                            FONTE_RODAPE
                    ),
                    MARGEM_LATERAL,
                    ALTURA_RODAPE / 2 - 3,
                    0
            );


            /*
             * LOGO
             */

            try {

                Image logo =
                        Image.getInstance(
                                carregarRecurso(
                                        CAMINHO_LOGO
                                )
                        );

                logo.scaleToFit(
                        28f,
                        28f
                );


                float logoX =
                        LARGURA_PAGINA
                                - MARGEM_LATERAL
                                - logo.getScaledWidth();

                float logoY =
                        (ALTURA_RODAPE
                                - logo.getScaledHeight()) / 2;


                logo.setAbsolutePosition(
                        logoX,
                        logoY
                );


                cb.addImage(logo);


            } catch (Exception e) {

                // Rodapé continua sem logo.

            }
        }
    }


    /*
     * =====================================================
     * DEGRADÊ HORIZONTAL
     * =====================================================
     */

    private void desenharGradienteHorizontal(
            PdfContentByte cb,
            float x,
            float y,
            float largura,
            float altura,
            Color corInicial,
            Color corFinal
    ) {


        int faixas = 100;

        float larguraFaixa =
                largura / faixas;


        for (int i = 0; i < faixas; i++) {


            float posicaoRelativa =
                    (float) i
                            / (faixas - 1);


            Color cor =
                    interpolarCor(
                            corInicial,
                            corFinal,
                            posicaoRelativa
                    );


            cb.setColorFill(cor);


            cb.rectangle(
                    x + (i * larguraFaixa),
                    y,
                    larguraFaixa + 0.5f,
                    altura
            );


            cb.fill();
        }
    }


    /*
     * =====================================================
     * INTERPOLAÇÃO DE CORES
     * =====================================================
     */

    private Color interpolarCor(
            Color inicial,
            Color fim,
            float posicao
    ) {


        int r =
                Math.round(
                        inicial.getRed()
                                + (
                                fim.getRed()
                                        - inicial.getRed()
                        ) * posicao
                );


        int g =
                Math.round(
                        inicial.getGreen()
                                + (
                                fim.getGreen()
                                        - inicial.getGreen()
                        ) * posicao
                );


        int b =
                Math.round(
                        inicial.getBlue()
                                + (
                                fim.getBlue()
                                        - inicial.getBlue()
                        ) * posicao
                );


        return new Color(
                r,
                g,
                b
        );
    }


    /*
     * =====================================================
     * CARREGAMENTO DE RECURSOS
     * =====================================================
     */

    private byte[] carregarRecurso(
            String caminhoClasspath
    ) {


        try (
                InputStream is =
                        getClass()
                                .getResourceAsStream(
                                        caminhoClasspath
                                )
        ) {


            if (is == null) {

                throw new IllegalStateException(
                        "Recurso não encontrado no classpath: "
                                + caminhoClasspath
                );

            }


            return is.readAllBytes();


        } catch (IOException e) {


            throw new RuntimeException(
                    "Erro ao carregar recurso: "
                            + caminhoClasspath,
                    e
            );

        }
    }


    /*
     * =====================================================
     * ESPAÇAMENTO
     * =====================================================
     */

    private void adicionarEspaco(
            Document documento,
            float tamanho
    ) throws DocumentException {


        Paragraph espaco =
                new Paragraph(" ");


        espaco.setSpacingAfter(
                tamanho
        );


        documento.add(
                espaco
        );
    }


    /*
     * =====================================================
     * FORMATADORES
     * =====================================================
     */

    private String formatarKwp(
            BigDecimal valor
    ) {


        if (valor == null) {

            return "0,000";

        }


        DecimalFormat formato =
                new DecimalFormat(
                        "0.000",
                        DecimalFormatSymbols.getInstance(
                                LOCALE_BR
                        )
                );


        return formato.format(
                valor
        );
    }


    private String formatarKwh(
            BigDecimal valor
    ) {


        if (valor == null) {

            return "0,00";

        }


        DecimalFormat formato =
                new DecimalFormat(
                        "0.00",
                        DecimalFormatSymbols.getInstance(
                                LOCALE_BR
                        )
                );


        return formato.format(
                valor
        );
    }


    private String formatarMoeda(
            BigDecimal valor
    ) {


        if (valor == null) {

            return "R$ 0,00";

        }


        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        LOCALE_BR
                );


        return formato.format(
                valor
        );
    }
}