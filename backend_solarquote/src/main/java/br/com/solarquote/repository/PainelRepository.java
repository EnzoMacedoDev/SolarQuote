package br.com.solarquote.repository;

import br.com.solarquote.entity.PainelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PainelRepository extends JpaRepository<PainelEntity, Long> {

    List<PainelEntity> findByAtivoTrue();

}
