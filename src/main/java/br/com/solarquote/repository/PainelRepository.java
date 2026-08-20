package br.com.solarquote.repository;

import br.com.solarquote.entity.Painel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PainelRepository extends JpaRepository<Painel, Long> {
}