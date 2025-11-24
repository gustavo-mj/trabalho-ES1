package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.*;
import br.com.clinica.presentation.TelaVacinacao;
import br.com.clinica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControladorVacinacao {

	private final TelaVacinacao tela;
	private final VacinacaoRepository vacinacaoRepository;
	private final LoteVacinaRepository loteRepository;
	private final ProfissionalRepository profissionalRepository;
	private final AnimalRepository animalRepository;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	public ControladorVacinacao(TelaVacinacao tela, VacinacaoRepository vacinacaoRepository,
			LoteVacinaRepository loteRepository, ProfissionalRepository profissionalRepository,
			AnimalRepository animalRepository) {
		this.tela = tela;
		this.vacinacaoRepository = vacinacaoRepository;
		this.loteRepository = loteRepository;
		this.profissionalRepository = profissionalRepository;
		this.animalRepository = animalRepository;
	}

	// --- NOVO MÉTODO PARA CORRIGIR O ERRO ---
	public void exibirMenu(Usuario usuario) {
		boolean continua = true;
		while (continua) {
			// Chama o menu da tela
			int opcao = tela.exibirMenu(usuario.getRole());

			switch (opcao) {
			case 0: // Registrar Aplicação
				registrarVacinacao();
				break;
			case 1: // Voltar
				continua = false;
				break;
			default:
				continua = false;
				break;
			}
		}
	}
	// ----------------------------------------

	@Transactional
	public void registrarVacinacao() { // Mudei de private para public para facilitar, se precisar chamar direto
		// 1. Carregar dados para o formulário
		List<Profissional> profissionais = profissionalRepository.findByRole(Role.VETERINARIO);

		if (profissionais.isEmpty()) {
			tela.mostraMensagem("Nenhum veterinário cadastrado para realizar o procedimento.");
			return;
		}

		List<Animal> animais = animalRepository.findAll();
		if (animais.isEmpty()) {
			tela.mostraMensagem("Nenhum animal cadastrado.");
			return;
		}

		String[] nomesVets = profissionais.stream().map(Profissional::getNome).toArray(String[]::new);
		String[] nomesAnimais = animais.stream().map(Animal::getNome).toArray(String[]::new);

		// 2. Pegar dados básicos
		String[] dados = tela.pegarDadosVacinacao(nomesAnimais, nomesVets);
		if (dados == null) return;

		int vetIndex = Integer.parseInt(dados[0]);
		int animalIndex = Integer.parseInt(dados[1]);
		TipoVacina tipo = TipoVacina.valueOf(dados[2]);
		String dataStr = dados[3];

		// 3. Buscar e Selecionar Lote ABERTO
		List<LoteVacina> lotesAbertos = loteRepository.findByTipoVacinaAndEstado(tipo, EstadoLote.ABERTO);
		LoteVacina lote = tela.selecionarLoteDisponivel(lotesAbertos);

		if (lote == null) return;

		try {
			// 4. Efetivar Vacinação
			Profissional vet = profissionais.get(vetIndex);
			Animal animal = animais.get(animalIndex);
			LocalDate data = LocalDate.parse(dataStr, formatter);

			// Cria o registro
			Vacinacao vacinacao = new Vacinacao(vet, animal, lote, data);
			vacinacaoRepository.save(vacinacao);

			// 5. Atualizar Estoque
			lote.setQuantidadeDoses(lote.getQuantidadeDoses() - 1);

			if (lote.getQuantidadeDoses() <= 0) {
				lote.setEstado(EstadoLote.ESGOTADO);
				tela.mostraMensagem("Atenção: Este lote acabou de se ESGOTAR.");
			}

			loteRepository.save(lote);
			tela.mostraMensagem("Vacinação registrada com sucesso!\nEstoque atualizado.");

		} catch (Exception e) {
			tela.mostraMensagem("Erro ao registrar: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
