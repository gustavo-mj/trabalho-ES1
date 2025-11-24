package br.com.clinica.repository;

import br.com.clinica.domain.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

	// Traz a consulta + dados do vet + dados do animal de uma vez só
	@Query("SELECT c FROM Consulta c JOIN FETCH c.veterinario JOIN FETCH c.animal")
	List<Consulta> findAllComDetalhes();
}
