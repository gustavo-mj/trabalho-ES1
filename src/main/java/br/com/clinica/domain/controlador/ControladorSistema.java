package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.Role;
import br.com.clinica.domain.model.StatusUsuario;
import br.com.clinica.domain.model.Usuario;
import br.com.clinica.presentation.TelaMenu;
import br.com.clinica.repository.UsuarioRepository; // 1. IMPORTE
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 2. IMPORTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- ADICIONE ESTA LINHA

@Service
public class ControladorSistema {

	private final ControladorAnimal controladorAnimal;
	private final ControladorTutor controladorTutor;
	private final ControladorProfissional controladorProfissional;
	private final ControladorLotacao controladorLotacao;
	private final ControladorEstoque controladorEstoque;
	private final ControladorVacinacao controladorVacinacao;
	private final TelaMenu telaMenu;

	// 3. NOVAS DEPENDÊNCIAS
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	private Usuario usuarioLogado;

	// 4. CONSTRUTOR ATUALIZADO
	@Autowired
	public ControladorSistema(ControladorAnimal controladorAnimal,
			ControladorTutor controladorTutor,
			ControladorProfissional controladorProfissional,
			ControladorLotacao controladorLotacao,
			TelaMenu telaMenu,
			UsuarioRepository usuarioRepository, // Injeta
			PasswordEncoder passwordEncoder,
			ControladorEstoque controladorEstoque,
			ControladorVacinacao controladorVacinacao) { // Injeta
		this.controladorAnimal = controladorAnimal;
		this.controladorTutor = controladorTutor;
		this.controladorProfissional = controladorProfissional;
		this.controladorLotacao = controladorLotacao;
		this.telaMenu = telaMenu;
		this.usuarioRepository = usuarioRepository; // Atribui
		this.passwordEncoder = passwordEncoder; // Atribui
		this.controladorEstoque = controladorEstoque;
		this.controladorVacinacao = controladorVacinacao;
	}

	public void inicializaSistema(Usuario usuario) {
		this.usuarioLogado = usuario;
		// --- LÓGICA DO STATUS NOVO ATUALIZADA ---
		if (usuarioLogado.getStatus() == StatusUsuario.NOVO) {
			telaMenu.mostraMensagem("Primeiro Acesso",
					"Bem-vindo! Para sua segurança, defina uma senha pessoal para continuar.");

			boolean senhaAlterada = false;

			// Loop obriga a trocar.
			while (!senhaAlterada) {
				// Chama o método específico para primeiro acesso
				boolean sucesso = realizarTrocaSenhaObrigatoria();

				if (sucesso) {
					senhaAlterada = true;
					// Atualiza o objeto em memória
					usuarioLogado = usuarioRepository.findById(usuarioLogado.getId()).orElse(usuarioLogado);
				} else {
					telaMenu.mostraMensagem("Atenção", "A definição da senha é obrigatória. O sistema será encerrado.");
					System.exit(0);
				}
			}
		}
		// -----------------------------

		System.out.println("Usuário logado: " + usuario.getUsername());
		abreMenuPrincipal();
	}

	/**
	 * NOVO MÉTODO: Trata especificamente a troca de senha do primeiro acesso.
	 * Não pede a senha antiga.
	 */
	@Transactional
	private boolean realizarTrocaSenhaObrigatoria() {
		// 1. Usa o novo método da tela (só 2 campos)
		String[] senhas = telaMenu.pegarDadosSenhaInicial();

		if (senhas == null) {
			return false; // Cancelou
		}

		String novaSenha = senhas[0];
		String confirmaNovaSenha = senhas[1];

		// 2. Validações (sem checar senha antiga)
		if (novaSenha.length() < 4) {
			telaMenu.mostraMensagem("Erro", "A senha deve ter pelo menos 4 caracteres.");
			return false;
		}

		if (!novaSenha.equals(confirmaNovaSenha)) {
			telaMenu.mostraMensagem("Erro", "A 'Nova Senha' e a 'Confirmação' não conferem.");
			return false;
		}

		// 3. Salva e Atualiza Status
		String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
		usuarioLogado.setPassword(novaSenhaCriptografada);
		usuarioLogado.setStatus(StatusUsuario.ATIVO); // Muda status para ATIVO

		usuarioRepository.save(usuarioLogado);
		telaMenu.mostraMensagem("Sucesso", "Senha definida com sucesso! Você agora está ATIVO.");

		return true;
	}

	private void abreMenuPrincipal() {
		Role role = usuarioLogado.getRole();

		while (true) {
			int opcao = telaMenu.exibirMenu(role);

			switch (opcao) {
			case 1: // Profissionais
				if (role == Role.ADMIN) {
					abreTelaProfissionais();
				} else {
					telaMenu.mostraMensagemAcessoNegado();
				}
				break;
			case 2: // Tutores
				if (role == Role.ADMIN || role == Role.SECRETARIA) {
					abreTelaTutores();
				} else {
					telaMenu.mostraMensagemAcessoNegado();
				}
				break;
			case 3: // Animais
				if (role == Role.ADMIN || role == Role.SECRETARIA) {
					abreTelaAnimais();
				} else {
					telaMenu.mostraMensagemAcessoNegado();
				}
				break;
			case 4: // Lotações
				if (role == Role.ADMIN) {
					abreTelaLotacoes();
				} else {
					telaMenu.mostraMensagemAcessoNegado();
				}
				break;
			case 5: // 5. NOVA OPÇÃO
				abreTelaAlterarSenha(false);
				break;
			case 6:
				abreTelaEstoque();
				break;
			case 7:
				abreTelaVacinacao();
				break;
			case 0:
				encerraSistema();
				break;
			default:
				// Não faz nada, apenas re-exibe o menu
				break;
			}
		}
	}

	/**
	 * Método refatorado para aceitar um booleano indicando se é o fluxo de primeiro acesso
	 * Retorna true se a senha foi alterada com sucesso, false se cancelou/falhou
	 */
	@Transactional
	private boolean abreTelaAlterarSenha(boolean primeiroAcesso) {
		String[] senhas = telaMenu.pegarDadosNovaSenha();
		if (senhas == null) {
			return false; // Usuário cancelou
		}

		String senhaAntiga = senhas[0];
		String novaSenha = senhas[1];
		String confirmaNovaSenha = senhas[2];

		// Se for primeiro acesso, a senha antiga é a padrão (CPF). Validamos igual.
		if (!passwordEncoder.matches(senhaAntiga, usuarioLogado.getPassword())) {
			telaMenu.mostraMensagem("Erro", "A 'Senha Antiga' está incorreta.");
			return false;
		}

		if (novaSenha.length() < 4) {
			telaMenu.mostraMensagem("Erro", "A 'Nova Senha' deve ter pelo menos 4 caracteres.");
			return false;
		}

		if (!novaSenha.equals(confirmaNovaSenha)) {
			telaMenu.mostraMensagem("Erro", "A 'Nova Senha' e a 'Confirmação' não são iguais.");
			return false;
		}

		// --- ATUALIZAÇÃO DO STATUS ---
		String novaSenhaCriptografada = passwordEncoder.encode(novaSenha);
		usuarioLogado.setPassword(novaSenhaCriptografada);

		// Se estava NOVO, vira ATIVO
		if (usuarioLogado.getStatus() == StatusUsuario.NOVO) {
			usuarioLogado.setStatus(StatusUsuario.ATIVO);
		}

		usuarioRepository.save(usuarioLogado);
		telaMenu.mostraMensagem("Sucesso", "Senha alterada com sucesso!");

		return true;
	}

	private void abreTelaAnimais() { controladorAnimal.exibirMenu(usuarioLogado); }
	private void abreTelaTutores() { controladorTutor.exibirMenu(usuarioLogado); }
	private void abreTelaProfissionais() { controladorProfissional.exibirMenu(usuarioLogado); }
	private void abreTelaLotacoes() { controladorLotacao.exibirMenu(usuarioLogado); }
	private void abreTelaEstoque() { controladorEstoque.exibirMenu((usuarioLogado));}
	private void abreTelaVacinacao() { controladorVacinacao.exibirMenu((usuarioLogado));}

	private void encerraSistema() {
		System.out.println("Encerrando o sistema...");
		System.exit(0);
	}
}
