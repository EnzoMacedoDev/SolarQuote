package br.com.solarquote.service;

import br.com.solarquote.entity.Orcamento;
import br.com.solarquote.entity.Painel;
import br.com.solarquote.entity.Cliente;
import br.com.solarquote.entity.Usuario;
import br.com.solarquote.repository.OrcamentoRepository;
import br.com.solarquote.repository.PainelRepository;
import br.com.solarquote.repository.ClienteRepository;
import br.com.solarquote.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PainelRepository painelRepository;

    public OrcamentoService(
            OrcamentoRepository orcamentoRepository,
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            PainelRepository painelRepository) {

        this.orcamentoRepository = orcamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.painelRepository = painelRepository;
    }

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public Optional<Orcamento> buscarPorId(Long id) {
        return orcamentoRepository.findById(id);
    }

    public Optional<Orcamento> salvar(
            Orcamento orcamento,
            Long usuarioId,
            Long clienteId,
            Long painelId) {

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        Optional<Painel> painel = painelRepository.findById(painelId);

        if (usuario.isEmpty() || cliente.isEmpty() || painel.isEmpty()) {
            return Optional.empty();
        }

        orcamento.setUsuario(usuario.get());
        orcamento.setCliente(cliente.get());
        orcamento.setPainel(painel.get());

        return Optional.of(orcamentoRepository.save(orcamento));
    }

    public Optional<Orcamento> atualizar(
            Long id,
            Orcamento dados,
            Long usuarioId,
            Long clienteId,
            Long painelId) {

        Optional<Orcamento> resultado = orcamentoRepository.findById(id);

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        Optional<Cliente> cliente = clienteRepository.findById(clienteId);
        Optional<Painel> painel = painelRepository.findById(painelId);

        if (usuario.isEmpty() || cliente.isEmpty() || painel.isEmpty()) {
            return Optional.empty();
        }

        Orcamento orcamento = resultado.get();

        orcamento.setUsuario(usuario.get());
        orcamento.setCliente(cliente.get());
        orcamento.setPainel(painel.get());

        orcamento.setQuantidadePaineis(dados.getQuantidadePaineis());
        orcamento.setPotenciaKwp(dados.getPotenciaKwp());
        orcamento.setGeracaoKwh(dados.getGeracaoKwh());
        orcamento.setPrecoEntrada(dados.getPrecoEntrada());
        orcamento.setPrecoInstalacao(dados.getPrecoInstalacao());
        orcamento.setValorTotal(dados.getValorTotal());
        orcamento.setPdf(dados.getPdf());

        return Optional.of(orcamentoRepository.save(orcamento));
    }

    public boolean excluir(Long id) {

        if (!orcamentoRepository.existsById(id)) {
            return false;
        }

        orcamentoRepository.deleteById(id);

        return true;
    }
}