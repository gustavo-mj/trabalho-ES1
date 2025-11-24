package br.com.clinica.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "profissional_cpf")
	private Profissional veterinario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "animal_microchip")
	private Animal animal;

	private LocalDate dataConsulta;

	// --- CAMPOS SUGERIDOS ---
	@Column(columnDefinition = "TEXT") // Permite textos longos
	private String motivo;

	@Column(columnDefinition = "TEXT")
	private String diagnostico;

	@Column(columnDefinition = "TEXT")
	private String tratamento;

	public Consulta() {}

	public Consulta(Profissional veterinario, Animal animal, LocalDate dataConsulta, String motivo, String diagnostico, String tratamento) {
		this.veterinario = veterinario;
		this.animal = animal;
		this.dataConsulta = dataConsulta;
		this.motivo = motivo;
		this.diagnostico = diagnostico;
		this.tratamento = tratamento;
	}

	// Getters e Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Profissional getVeterinario() { return veterinario; }
	public void setVeterinario(Profissional veterinario) { this.veterinario = veterinario; }
	public Animal getAnimal() { return animal; }
	public void setAnimal(Animal animal) { this.animal = animal; }
	public LocalDate getDataConsulta() { return dataConsulta; }
	public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }
	public String getMotivo() { return motivo; }
	public void setMotivo(String motivo) { this.motivo = motivo; }
	public String getDiagnostico() { return diagnostico; }
	public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
	public String getTratamento() { return tratamento; }
	public void setTratamento(String tratamento) { this.tratamento = tratamento; }
}
