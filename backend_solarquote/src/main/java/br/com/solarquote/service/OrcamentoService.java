package br.com.solarquote.service;

import br.com.solarquote.dto.ItemOrcamentoRequest;
import br.com.solarquote.dto.OrcamentoRequest;
import br.com.solarquote.dto.OrcamentoResponse;
import br.com.solarquote.entity.ItemOrcamentoEntity;
import br.com.solarquote.entity.OrcamentoEntity;
import br.com.solarquote.entity.PainelEntity;
import br.com.solarquote.entity.UsuarioEntity;
import br.com.solarquote.repository.OrcamentoRepository;
import br.com.solarquote.repository.PainelRepository;
import br.com.solarquote.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    // regra fixa do SolarQuote: geracao (kWh) = potencia (kWp) x 127
    private static final BigDecimal FATOR_GERACAO = BigDecimal.valueOf(127);

    private final OrcamentoRepository orcamentoRepository;
    private final PainelRepository painelRepository;
    private final UsuarioRepository usuarioRepository;
    private final PdfService pdfService;

    public List<OrcamentoResponse> listar() {
        return orcamentoRepository.findAll().stream()
                .map(OrcamentoResponse::from)
                .toList();
    }

    public OrcamentoResponse buscarPorId(Long id) {
        return OrcamentoResponse.from(buscarEntityPorId(id));
    }

    public byte[] buscarPdf(Long id) {
        return buscarEntityPorId(id).getPdf();
    }

    public OrcamentoResponse criar(OrcamentoRequest request) {
        PainelEntity painel = buscarPainelValido(request.painelId());
        UsuarioEntity usuario = usuarioAutenticado();

        OrcamentoEntity orcamento = new OrcamentoEntity();
        orcamento.setNomeCliente(request.nomeCliente());
        orcamento.setLocalCliente(request.localCliente());
        orcamento.setPainel(painel);
        orcamento.setUsuario(usuario);
        aplicarCalculos(orcamento, painel, request);
        aplicarItens(orcamento, request.itens());

        // salva primeiro pra gerar id e dataCriacao (usados no PDF),
        // depois gera o PDF e salva de novo com o PDF preenchido
        OrcamentoEntity salvo = orcamentoRepository.save(orcamento);
        salvo.setPdf(pdfService.gerarPdfOrcamento(salvo));
        salvo = orcamentoRepository.save(salvo);

        return OrcamentoResponse.from(salvo);
    }

    public OrcamentoResponse atualizar(Long id, OrcamentoRequest request) {
        OrcamentoEntity existente = buscarEntityPorId(id);
        PainelEntity painel = buscarPainelValido(request.painelId());

        existente.setNomeCliente(request.nomeCliente());
        existente.setLocalCliente(request.localCliente());
        existente.setPainel(painel);
        aplicarCalculos(existente, painel, request);
        aplicarItens(existente, request.itens());
        existente.setPdf(pdfService.gerarPdfOrcamento(existente));

        return OrcamentoResponse.from(orcamentoRepository.save(existente));
    }

    public void excluir(Long id) {
        orcamentoRepository.delete(buscarEntityPorId(id));
    }

    private void aplicarCalculos(OrcamentoEntity orcamento, PainelEntity painel, OrcamentoRequest request) {
        BigDecimal potenciaW = BigDecimal.valueOf(request.quantidadePaineis())
                .multiply(BigDecimal.valueOf(painel.getPotenciaWp()));
        BigDecimal potenciaKwp = potenciaW.divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
        BigDecimal geracaoKwh = potenciaKwp.multiply(FATOR_GERACAO).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = request.precoEntrada().add(request.precoInstalacao());

        orcamento.setQuantidadePaineis(request.quantidadePaineis());
        orcamento.setPotenciaKwp(potenciaKwp);
        orcamento.setGeracaoKwh(geracaoKwh);
        orcamento.setPrecoEntrada(request.precoEntrada());
        orcamento.setPrecoInstalacao(request.precoInstalacao());
        orcamento.setValorTotal(valorTotal);
    }

    private void aplicarItens(OrcamentoEntity orcamento, List<ItemOrcamentoRequest> itensRequest) {
        // limpa a lista atual (orphanRemoval cuida de apagar do banco)
        // e recria do zero com o que veio na requisicao
        orcamento.getItens().clear();

        if (itensRequest == null) {
            return;
        }

        for (ItemOrcamentoRequest itemReq : itensRequest) {
            ItemOrcamentoEntity item = new ItemOrcamentoEntity();
            item.setDescricao(itemReq.descricao());
            item.setQuantidade(itemReq.quantidade());
            item.setOrcamento(orcamento);
            orcamento.getItens().add(item);
        }
    }

    private PainelEntity buscarPainelValido(Long painelId) {
        PainelEntity painel = painelRepository.findById(painelId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Painel não encontrado com id " + painelId));

        if (!painel.getAtivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Painel está desativado");
        }

        return painel;
    }

    private OrcamentoEntity buscarEntityPorId(Long id) {
        return orcamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Orçamento não encontrado com id " + id));
    }

    private UsuarioEntity usuarioAutenticado() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado"));
    }
}