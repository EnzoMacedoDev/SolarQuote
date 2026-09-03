package br.com.solarquote.service;

import br.com.solarquote.entity.PainelEntity;
import br.com.solarquote.repository.PainelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PainelService {

    private final PainelRepository painelRepository;

    public List<PainelEntity> listar() {
        return painelRepository.findAll();
    }

    public PainelEntity buscarPorId(Long id) {
        return painelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Painel não encontrado com id " + id));
    }

    public PainelEntity criar(PainelEntity dados) {
        // cria uma entity nova do zero, em vez de reusar "dados" direto,
        // assim nao importa o que vier no id (mesmo se o cliente mandar
        // um id sem querer, ele nunca chega a ser usado)
        PainelEntity novo = new PainelEntity();
        novo.setModelo(dados.getModelo());
        novo.setFabricante(dados.getFabricante());
        novo.setPotenciaWp(dados.getPotenciaWp());
        novo.setPreco(dados.getPreco());
        novo.setAtivo(dados.getAtivo());
        return painelRepository.save(novo);
    }

    public PainelEntity atualizar(Long id, PainelEntity dadosNovos) {
        PainelEntity existente = buscarPorId(id);
        existente.setModelo(dadosNovos.getModelo());
        existente.setFabricante(dadosNovos.getFabricante());
        existente.setPotenciaWp(dadosNovos.getPotenciaWp());
        existente.setPreco(dadosNovos.getPreco());
        existente.setAtivo(dadosNovos.getAtivo());
        return painelRepository.save(existente);
    }

    public void desativar(Long id) {
        PainelEntity painel = buscarPorId(id);
        painel.setAtivo(false);
        painelRepository.save(painel);
    }
}