package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.*;
import br.com.clinica.presentation.TelaConsulta;
import br.com.clinica.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ControladorConsulta {

	private final TelaConsulta tela;
	private final ConsultaRepository consultaRepository;
	private final ProfissionalRepository profissionalRepository;
	private final AnimalRepository animalRepository;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	public ControladorConsulta(TelaConsulta tela, ConsultaRepository consultaRepository,
			ProfissionalRepository profissionalRepository, AnimalRepository animalRepository) {
		this.tela = tela;
		this.consultaRepository = consultaRepository;
		this.profissionalRepository = profissionalRepository;
		this.animalRepository = animalRepository;
	}

	public void exibirMenu(Usuario usuario) {
		boolean continua = true;
		while (continua) {
			int opcao = tela.exibirMenu(usuario.getRole());
			switch (opcao) {
			case 0: cadastrarConsulta(); break;
			case 1: alterarConsulta(); break;
			case 2: listarConsultas(); break;
			case 3: excluirConsulta(); break;
			case 4: case 99: continua = false; break;
			default: tela.mostraMensagem("Opção inválida ou acesso negado."); break;
			}
		}
	}

	private List<Profissional> getVeterinarios() {
		// Usa o método otimizado do repositório que criamos antes
		return profissionalRepository.findByRole(Role.VETERINARIO);
	}

	@Transactional
	private void cadastrarConsulta() {
		List<Profissional> vets = getVeterinarios();
		if (vets.isEmpty()) { tela.mostraMensagem("Nenhum veterinário disponível."); return; }

		List<Animal> animais = animalRepository.findAll();
		if (animais.isEmpty()) { tela.mostraMensagem("Nenhum animal cadastrado."); return; }

		String[] nomesVets = vets.stream().map(Profissional::getNome).toArray(String[]::new);
		String[] nomesAnimais = animais.stream().map(Animal::getNome).toArray(String[]::new);

		String[] dados = tela.pegarDados(nomesVets, nomesAnimais);
		if (dados == null) return;

		try {
			Profissional vet = vets.get(Integer.parseInt(dados[0]));
			Animal animal = animais.get(Integer.parseInt(dados[1]));
			LocalDate data = LocalDate.parse(dados[2], formatter);
			String motivo = dados[3];
			String diag = dados[4];
			String trat = dados[5];

			Consulta c = new Consulta(vet, animal, data, motivo, diag, trat);
			consultaRepository.save(c);
			tela.mostraMensagem("Consulta registrada com sucesso!");
		} catch (Exception e) {
			tela.mostraMensagem("Erro ao registrar: " + e.getMessage());
		}
	}

	@Transactional
	private void alterarConsulta() {
		// Carrega com detalhes para o Seletor funcionar sem LazyException
		List<Consulta> lista = consultaRepository.findAllComDetalhes();
		Long id = tela.selecionarConsulta(lista);
		if (id == null) return;

		Consulta c = consultaRepository.findById(id).orElse(null);
		if (c == null) return;

		List<Profissional> vets = getVeterinarios();
		List<Animal> animais = animalRepository.findAll();

		String[] nomesVets = vets.stream().map(Profissional::getNome).toArray(String[]::new);
		String[] nomesAnimais = animais.stream().map(Animal::getNome).toArray(String[]::new);

		// Encontra índices atuais para pré-selecionar (lógica simples)
		int idxVet = vets.indexOf(c.getVeterinario());
		int idxAni = animais.indexOf(c.getAnimal());

		String[] novosDados = tela.pegarDadosAlteracao(c, nomesVets, nomesAnimais, idxVet, idxAni);
		if (novosDados == null) return;

		try {
			c.setVeterinario(vets.get(Integer.parseInt(novosDados[0])));
			c.setAnimal(animais.get(Integer.parseInt(novosDados[1])));
			c.setDataConsulta(LocalDate.parse(novosDados[2], formatter));
			c.setMotivo(novosDados[3]);
			c.setDiagnostico(novosDados[4]);
			c.setTratamento(novosDados[5]);

			consultaRepository.save(c);
			tela.mostraMensagem("Consulta atualizada!");
		} catch (Exception e) {
			tela.mostraMensagem("Erro ao atualizar: " + e.getMessage());
		}
	}

	@Transactional(readOnly = true)
	private void listarConsultas() {
		// Usa o método com JOIN FETCH para evitar erro de Lazy Loading na tela
		tela.mostraLista(consultaRepository.findAllComDetalhes());
	}

	@Transactional
	private void excluirConsulta() {
		List<Consulta> lista = consultaRepository.findAllComDetalhes();
		Long id = tela.selecionarConsulta(lista);
		if (id != null) {
			consultaRepository.deleteById(id);
			tela.mostraMensagem("Consulta removida.");
		}
	}
}
