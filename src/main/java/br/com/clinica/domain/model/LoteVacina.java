package br.com.clinica.domain.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class LoteVacina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String numeroLote;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TipoVacina tipoVacina;

	private String fabricante;
	private int quantidadeDoses;

	private LocalDate dataFabricacao;
	private LocalDate dataValidade;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private EstadoLote estado; // Campo Novo

	public LoteVacina() {}

	public LoteVacina(String numeroLote, TipoVacina tipoVacina, String fabricante, int quantidadeDoses, LocalDate dataFabricacao, LocalDate dataValidade) {
		this.numeroLote = numeroLote;
		this.tipoVacina = tipoVacina;
		this.fabricante = fabricante;
		this.quantidadeDoses = quantidadeDoses;
		this.dataFabricacao = dataFabricacao;
		this.dataValidade = dataValidade;
		this.estado = EstadoLote.SELADO; // Padrão ao criar
	}

	// Getters e Setters
	public Long getId() { return id; }
	public String getNumeroLote() { return numeroLote; }
	public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
	public TipoVacina getTipoVacina() { return tipoVacina; }
	public void setTipoVacina(TipoVacina tipoVacina) { this.tipoVacina = tipoVacina; }
	public String getFabricante() { return fabricante; }
	public void setFabricante(String fabricante) { this.fabricante = fabricante; }
	public int getQuantidadeDoses() { return quantidadeDoses; }
	public void setQuantidadeDoses(int quantidadeDoses) { this.quantidadeDoses = quantidadeDoses; }
	public LocalDate getDataFabricacao() { return dataFabricacao; }
	public void setDataFabricacao(LocalDate dataFabricacao) { this.dataFabricacao = dataFabricacao; }
	public LocalDate getDataValidade() { return dataValidade; }
	public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
	public EstadoLote getEstado() { return estado; }
	public void setEstado(EstadoLote estado) { this.estado = estado; }
}
