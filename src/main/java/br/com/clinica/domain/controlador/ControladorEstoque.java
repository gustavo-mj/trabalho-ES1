package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.*;
import br.com.clinica.presentation.TelaEstoqueVacina;
import br.com.clinica.repository.LoteVacinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ControladorEstoque {

	private final TelaEstoqueVacina tela;
	private final LoteVacinaRepository repository;
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	public ControladorEstoque(TelaEstoqueVacina tela, LoteVacinaRepository repository) {
		this.tela = tela;
		this.repository = repository;
	}

	public void exibirMenu(Usuario usuario) {
		boolean continua = true;
		while (continua) {
			int opcao = tela.exibirMenu(usuario.getRole());
			switch (opcao) {
			case 0: // Cadastrar
				if(usuario.getRole() != Role.VETERINARIO) cadastrarLote();
				else tela.mostraMensagemAcessoNegado();
				break;
			case 1: // Listar
				listarLotesPorTipo();
				break;
			case 2: // Gerenciar (Abrir/Invalidar)
				gerenciarLotes();
				break;
			case 3: // Excluir
				if(usuario.getRole() == Role.ADMIN) excluirLote();
				else tela.mostraMensagemAcessoNegado();
				break;
			case 4: // Voltar
			case 99:
				continua = false;
				break;
			}
		}
	}

	@Transactional
	private void cadastrarLote() {
		String[] dados = tela.pegarDados();
		if (dados == null) return;
		try {
			// ... conversões ...
			String numeroLote = dados[0];
			String fabricante = dados[1];
			int qtd = Integer.parseInt(dados[2]);
			TipoVacina tipo = TipoVacina.valueOf(dados[3]);
			LocalDate dtFab = LocalDate.parse(dados[4], formatter);
			LocalDate dtVal = LocalDate.parse(dados[5], formatter);

			if (dtVal.isBefore(dtFab)) {
				tela.mostraMensagem("Erro: Validade anterior à fabricação.");
				return;
			}

			LoteVacina lote = new LoteVacina(numeroLote, tipo, fabricante, qtd, dtFab, dtVal);
			// O construtor já define ESTADO = SELADO
			repository.save(lote);
			tela.mostraMensagem("Lote cadastrado com sucesso!");

		} catch (Exception e) {
			tela.mostraMensagem("Erro ao cadastrar: " + e.getMessage());
		}
	}

	@Transactional
	private void gerenciarLotes() {
		// Busca apenas lotes SELADOS ou ABERTOS (para invalidar)
		// Simplificação: Vamos buscar os SELADOS para abrir
		List<LoteVacina> lotesSelados = repository.findByEstado(EstadoLote.SELADO);

		LoteVacina loteSelecionado = tela.selecionarLoteParaAcao(lotesSelados, "Abrir ou Invalidar");
		if (loteSelecionado == null) return;

		EstadoLote novoEstado = tela.escolherNovoEstado();
		if (novoEstado != null) {
			loteSelecionado.setEstado(novoEstado);
			repository.save(loteSelecionado);
			tela.mostraMensagem("Estado do lote atualizado para: " + novoEstado);
		}
	}

	@Transactional(readOnly = true)
	private void listarLotesPorTipo() {
		TipoVacina tipo = tela.selecionarTipoFiltro();
		if (tipo == null) return;
		tela.mostraLista(repository.findByTipoVacina(tipo));
	}

	@Transactional
	private void excluirLote() {
		TipoVacina tipo = tela.selecionarTipoFiltro();
		if(tipo == null) return;
		List<LoteVacina> lotes = repository.findByTipoVacina(tipo);
		Long id = tela.selecionarLoteParaExclusao(lotes);
		if (id != null) {
			repository.deleteById(id);
			tela.mostraMensagem("Lote excluído.");
		}
	}
}
