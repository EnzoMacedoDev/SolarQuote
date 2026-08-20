package br.com.solarquote.service;

import br.com.solarquote.entity.Painel;
import br.com.solarquote.repository.PainelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PainelService {

    private final PainelRepository painelRepository;

    public PainelService(PainelRepository painelRepository) {
        this.painelRepository = painelRepository;
    }

    public List<Painel> listarTodos() {
        return painelRepository.findAll();
    }

    public Optional<Painel> buscarPorId(Long id) {
        return painelRepository.findById(id);
    }

    public Painel salvar(Painel painel) {
        return painelRepository.save(painel);
    }

    public Optional<Painel> atualizar(Long id, Painel dados) {

        return painelRepository.findById(id)
                .map(painel -> {

                    painel.setModelo(dados.getModelo());
                    painel.setFabricante(dados.getFabricante());
                    painel.setPotenciaWp(dados.getPotenciaWp());
                    painel.setPreco(dados.getPreco());
                    painel.setAtivo(dados.getAtivo());

                    return painelRepository.save(painel);
                });
    }

    public boolean excluir(Long id) {

        if (!painelRepository.existsById(id)) {
            return false;
        }

        painelRepository.deleteById(id);
        return true;
    }
}