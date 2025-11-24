package br.com.clinica.repository;

import br.com.clinica.domain.model.Lotacao; // Importe Lotacao
import br.com.clinica.domain.model.Profissional;
import br.com.clinica.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, String> {

	// 1. Método para listar todos carregando a Lotação (usado no CRUD de Profissionais)
	@Query("SELECT p FROM Profissional p JOIN FETCH p.lotacao")
	List<Profissional> findAllComLotacao();

	// 2. Método para buscar por Role (usado na Vacinação)
	@Query("SELECT p FROM Profissional p JOIN FETCH p.lotacao l WHERE l.role = :role")
	List<Profissional> findByRole(@Param("role") Role role);

	// 3. --- MÉTODO QUE ESTAVA FALTANDO ---
	// Conta quantos profissionais possuem essa lotação (usado para validar exclusão de lotação)
	long countByLotacao(Lotacao lotacao);
}
