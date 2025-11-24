package br.com.clinica.presentation;

import br.com.clinica.domain.model.Role; // 1. IMPORTE ROLE
import javax.swing.JOptionPane; // Importe

public abstract class TelaCRUD {

	protected abstract String getTituloTela();

	public abstract int exibirMenu(Role role);

	public void mostraMensagemAcessoNegado() {
		JOptionPane.showMessageDialog(null,
				"Acesso Negado. Você não tem permissão para esta função.",
				"Erro de Permissão",
				JOptionPane.ERROR_MESSAGE);
	}
}
