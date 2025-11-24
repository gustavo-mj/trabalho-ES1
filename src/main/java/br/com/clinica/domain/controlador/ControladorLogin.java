package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.StatusUsuario;
import br.com.clinica.domain.model.Usuario;
import br.com.clinica.presentation.TelaLogin;
import br.com.clinica.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ControladorLogin {

	@Autowired
	private TelaLogin telaLogin;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Lógica atualizada para tratar Bloqueio e Tentativas.
	 */
	@Transactional // Importante para salvar o incremento das tentativas
	public Usuario autenticar() {
		while (true) {
			String[] credenciais = telaLogin.pegarCredenciais();

			if (credenciais == null) {
				return null; // Cancelou
			}

			String username = credenciais[0];
			String senhaDigitada = credenciais[1];

			Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);

			if (usuarioOpt.isPresent()) {
				Usuario usuario = usuarioOpt.get();

				// 1. Verifica se já está BLOQUEADO
				if (usuario.getStatus() == StatusUsuario.BLOQUEADO) {
					telaLogin.mostraMensagemErro("Acesso Bloqueado. Contate o Administrador.");
					continue; // Volta para o login
				}

				// 2. Verifica a senha
				if (passwordEncoder.matches(senhaDigitada, usuario.getPassword())) {
					// SUCESSO!
					// Zera as tentativas se tiver alguma
					if (usuario.getTentativasFalhas() > 0) {
						usuario.setTentativasFalhas(0);
						usuarioRepository.save(usuario);
					}
					return usuario;
				} else {
					// SENHA ERRADA
					int tentativas = usuario.getTentativasFalhas() + 1;
					usuario.setTentativasFalhas(tentativas);

					String msgErro = "Usuário ou senha inválidos.";

					// Verifica se deve BLOQUEAR (>= 3 erros)
					if (tentativas >= 3) {
						usuario.setStatus(StatusUsuario.BLOQUEADO);
						msgErro = "Número de tentativas excedido. Usuário BLOQUEADO.";
					} else {
						msgErro += "\nTentativa " + tentativas + " de 3.";
					}

					usuarioRepository.save(usuario); // Salva no banco o incremento ou bloqueio
					telaLogin.mostraMensagemErro(msgErro);
				}

			} else {
				// Usuário não encontrado (por segurança, não dizemos que não existe, mas não incrementamos contador de ninguém)
				telaLogin.mostraMensagemErro("Usuário ou senha inválidos.");
			}
		}
	}
}
