package br.com.clinica.presentation;

import br.com.clinica.domain.model.Role;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class TelaMenu {
	private JFrame frame;
	private int opcaoSelecionada = 0;

	public int exibirMenu(Role role) {
		frame = new JFrame("Sistema da Clínica Veterinária - Logado como: " + role.name());
		frame.setSize(400, 350); // Aumentei um pouco a altura
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		JLabel titulo = new JLabel("Bem-vindo ao Sistema!", JLabel.CENTER);
		titulo.setFont(new Font("Arial", Font.BOLD, 18));
		frame.add(titulo, BorderLayout.NORTH);

		JPanel panelOpcoes = new JPanel();
		panelOpcoes.setLayout(new BoxLayout(panelOpcoes, BoxLayout.Y_AXIS));

		// --- OPÇÕES VISÍVEIS PARA TODOS ---
		JRadioButton alterarSenha = new JRadioButton("Alterar Minha Senha"); // 1. NOVO BOTÃO
		JRadioButton sair = new JRadioButton("Sair");

		ButtonGroup grupoOpcoes = new ButtonGroup();
		grupoOpcoes.add(alterarSenha); // Adiciona ao grupo
		grupoOpcoes.add(sair);

		// --- LÓGICA DE PERMISSÃO DA TELA ---
		if (role == Role.ADMIN) {
			JRadioButton profissionais = new JRadioButton("Profissionais");
			JRadioButton tutores = new JRadioButton("Tutores");
			JRadioButton animais = new JRadioButton("Animais");
			JRadioButton lotacoes = new JRadioButton("Lotação");
			JRadioButton estoque = new JRadioButton("Estoque de Vacinas");
			JRadioButton vacinacao = new JRadioButton("Vacinação");
			grupoOpcoes.add(profissionais);
			grupoOpcoes.add(tutores);
			grupoOpcoes.add(animais);
			grupoOpcoes.add(lotacoes);
			grupoOpcoes.add(estoque);
			grupoOpcoes.add(vacinacao);
			panelOpcoes.add(profissionais);
			panelOpcoes.add(tutores);
			panelOpcoes.add(animais);
			panelOpcoes.add(lotacoes);
			panelOpcoes.add(estoque);
			panelOpcoes.add(vacinacao);
			JRadioButton consultas = new JRadioButton("Consultas");
			grupoOpcoes.add(consultas);
			panelOpcoes.add(consultas);
		} else if (role == Role.SECRETARIA) {
			JRadioButton tutores = new JRadioButton("Tutores");
			JRadioButton animais = new JRadioButton("Animais");
			JRadioButton estoque = new JRadioButton("Estoque de Vacinas");
			JRadioButton vacinacao = new JRadioButton("Vacinação");
			grupoOpcoes.add(tutores);
			grupoOpcoes.add(animais);
			grupoOpcoes.add(estoque);
			grupoOpcoes.add(vacinacao);
			panelOpcoes.add(tutores);
			panelOpcoes.add(animais);
			panelOpcoes.add(estoque);
			panelOpcoes.add(vacinacao);
			JRadioButton consultas = new JRadioButton("Consultas");
			grupoOpcoes.add(consultas);
			panelOpcoes.add(consultas);
		} else if (role == Role.VETERINARIO) {
			JRadioButton vacinacao = new JRadioButton("Vacinação");
			JRadioButton estoque = new JRadioButton("Estoque de Vacinas");
			grupoOpcoes.add(vacinacao);
			grupoOpcoes.add(estoque);
			panelOpcoes.add(vacinacao);
			panelOpcoes.add(estoque);
			JRadioButton consultas = new JRadioButton("Consultas");
			grupoOpcoes.add(consultas);
			panelOpcoes.add(consultas);
		}

		// Adiciona as opções comuns no final
		panelOpcoes.add(new JSeparator()); // Linha divisória
		panelOpcoes.add(alterarSenha);
		panelOpcoes.add(sair);

		frame.add(panelOpcoes, BorderLayout.CENTER);

		JPanel panelBotoes = new JPanel();
		JButton confirmar = new JButton("Confirmar");
		JButton cancelar = new JButton("Cancelar");

		confirmar.addActionListener((ActionEvent e) -> {
			opcaoSelecionada = -1; // -1 = Opção inválida/nenhuma

			// 2. MAPEIA OS CLIQUES PARA OS NÚMEROS
			// (Temos que remapear os botões, pois o 'profissionais' pode não existir)
			if (sair.isSelected()) opcaoSelecionada = 0;
			else if (alterarSenha.isSelected()) opcaoSelecionada = 5; // 3. NOVA OPÇÃO
				// Apenas checa as outras opções se elas existirem (baseado no 'isSelected')
			else if (e.getSource() instanceof JRadioButton && ((JRadioButton) e.getSource()).isSelected()) {
				String textoBotao = ((JRadioButton) e.getSource()).getText();
				if (textoBotao.equals("Profissionais")) opcaoSelecionada = 1;
				else if (textoBotao.equals("Tutores")) opcaoSelecionada = 2;
				else if (textoBotao.equals("Animais")) opcaoSelecionada = 3;
				else if (textoBotao.equals("Lotação")) opcaoSelecionada = 4;
				else if (textoBotao.equals("Estoque de Vacinas")) opcaoSelecionada = 6;
				else if (textoBotao.equals("Vacinação")) opcaoSelecionada = 7;
				else if (textoBotao.equals("Consultas")) opcaoSelecionada = 8;
			}

			// Hack para garantir que os botões de Admin/Secretaria sejam encontrados
			// Se a opção ainda for -1, varre os botões
			if (opcaoSelecionada == -1) {
				for (java.awt.Component comp : panelOpcoes.getComponents()) {
					if (comp instanceof JRadioButton) {
						JRadioButton btn = (JRadioButton) comp;
						if (btn.isSelected()) {
							String texto = btn.getText();
							if (btn.getText().equals("Profissionais")) opcaoSelecionada = 1;
							else if (texto.equals("Tutores")) opcaoSelecionada = 2;
							else if (texto.equals("Animais")) opcaoSelecionada = 3;
							else if (texto.equals("Lotação")) opcaoSelecionada = 4;
							else if (texto.equals("Estoque de Vacinas")) opcaoSelecionada = 6;
							else if (texto.equals("Vacinação")) opcaoSelecionada = 7;
							else if (texto.equals("Consultas")) opcaoSelecionada = 8;
							break;
						}
					}
				}
			}

			frame.dispose();
		});

		cancelar.addActionListener((ActionEvent e) -> {
			opcaoSelecionada = 0; // Cancelar = Sair
			frame.dispose();
		});

		panelBotoes.add(confirmar);
		panelBotoes.add(cancelar);
		frame.add(panelBotoes, BorderLayout.SOUTH);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		while (frame.isDisplayable()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignored) {}
		}

		return opcaoSelecionada;
	}

	/**
	 * NOVO MÉTODO: Exibe formulário apenas para definir a nova senha (sem pedir a antiga).
	 * Usado no primeiro acesso.
	 */
	public String[] pegarDadosSenhaInicial() {
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		panel.setPreferredSize(new Dimension(350, 80));

		panel.add(new JLabel("Nova Senha:"));
		JPasswordField novaSenhaField = new JPasswordField();
		panel.add(novaSenhaField);

		panel.add(new JLabel("Confirmar Nova Senha:"));
		JPasswordField confirmaSenhaField = new JPasswordField();
		panel.add(confirmaSenhaField);

		// Foca no campo de nova senha ao abrir
		SwingUtilities.invokeLater(() -> novaSenhaField.requestFocusInWindow());

		int result = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Definição de Senha Pessoal (Primeiro Acesso)",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (result == JOptionPane.OK_OPTION) {
			return new String[]{
					new String(novaSenhaField.getPassword()),
					new String(confirmaSenhaField.getPassword())
			};
		}
		return null; // Cancelou
	}

	public String[] pegarDadosNovaSenha() {
		JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
		panel.setPreferredSize(new Dimension(350, 100));

		panel.add(new JLabel("Senha Antiga:"));
		JPasswordField senhaAntigaField = new JPasswordField();
		panel.add(senhaAntigaField);

		panel.add(new JLabel("Nova Senha:"));
		JPasswordField novaSenhaField = new JPasswordField();
		panel.add(novaSenhaField);

		panel.add(new JLabel("Confirmar Nova Senha:"));
		JPasswordField confirmaSenhaField = new JPasswordField();
		panel.add(confirmaSenhaField);

		SwingUtilities.invokeLater(() -> senhaAntigaField.requestFocusInWindow());

		int result = JOptionPane.showConfirmDialog(
				null,
				panel,
				"Alterar Senha",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);

		if (result == JOptionPane.OK_OPTION) {
			return new String[]{
					new String(senhaAntigaField.getPassword()),
					new String(novaSenhaField.getPassword()),
					new String(confirmaSenhaField.getPassword())
			};
		}
		return null; // Cancelou
	}

	public void mostraMensagemAcessoNegado() {
		JOptionPane.showMessageDialog(null,
				"Acesso Negado. Você não tem permissão para esta função.",
				"Erro de Permissão",
				JOptionPane.ERROR_MESSAGE);
	}

	public void mostraMensagem(String titulo, String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
}
