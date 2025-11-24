package br.com.clinica.repository;

import br.com.clinica.domain.model.Vacinacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacinacaoRepository extends JpaRepository<Vacinacao, Long> {
}
