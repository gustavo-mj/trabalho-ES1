package br.com.clinica.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Vacinacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "profissional_cpf")
	private Profissional responsavel;

	@ManyToOne
	@JoinColumn(name = "animal_microchip")
	private Animal animal;

	@ManyToOne
	@JoinColumn(name = "lote_id")
	private LoteVacina lote;

	private LocalDate dataAplicacao;

	public Vacinacao() {}

	public Vacinacao(Profissional responsavel, Animal animal, LoteVacina lote, LocalDate dataAplicacao) {
		this.responsavel = responsavel;
		this.animal = animal;
		this.lote = lote;
		this.dataAplicacao = dataAplicacao;
	}

	public Long getId() { return id; }
	public Profissional getResponsavel() { return responsavel; }
	public void setResponsavel(Profissional responsavel) { this.responsavel = responsavel; }
	public Animal getAnimal() { return animal; }
	public void setAnimal(Animal animal) { this.animal = animal; }
	public LoteVacina getLote() { return lote; }
	public void setLote(LoteVacina lote) { this.lote = lote; }
	public LocalDate getDataAplicacao() { return dataAplicacao; }
	public void setDataAplicacao(LocalDate dataAplicacao) { this.dataAplicacao = dataAplicacao; }
}
