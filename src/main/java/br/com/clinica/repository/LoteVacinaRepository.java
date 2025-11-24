package br.com.clinica.repository;

import br.com.clinica.domain.model.EstadoLote;
import br.com.clinica.domain.model.LoteVacina;
import br.com.clinica.domain.model.TipoVacina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteVacinaRepository extends JpaRepository<LoteVacina, Long> {
	List<LoteVacina> findByTipoVacina(TipoVacina tipo);

	// Busca lotes abertos de um tipo específico (para vacinação)
	List<LoteVacina> findByTipoVacinaAndEstado(TipoVacina tipo, EstadoLote estado);

	// Busca lotes por estado (para gerenciamento)
	List<LoteVacina> findByEstado(EstadoLote estado);
}
