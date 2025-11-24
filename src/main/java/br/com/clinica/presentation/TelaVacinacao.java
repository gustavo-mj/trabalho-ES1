package br.com.clinica.presentation;

import br.com.clinica.domain.model.LoteVacina;
import br.com.clinica.domain.model.Role; // Importe Role
import br.com.clinica.domain.model.TipoVacina;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

@Component
public class TelaVacinacao {

	// --- NOVO MÉTODO: MENU DA VACINAÇÃO ---
	public int exibirMenu(Role role) {
		String[] opcoes = {"Registrar Aplicação", "Voltar"};

		int escolha = JOptionPane.showOptionDialog(
				null, "Menu de Vacinação", "Vacinação",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, opcoes, opcoes[0]
		);

		// Mapeia: 0 -> Registrar, 1 (ou fechar) -> Voltar
		if (escolha == 0) return 0;
		return 1;
	}
	// ---------------------------------------

	public String[] pegarDadosVacinacao(String[] nomesAnimais, String[] nomesVets) {
		try {
			JPanel panel = new JPanel(new GridLayout(0, 1));

			JComboBox<String> comboVets = new JComboBox<>(nomesVets);
			panel.add(new JLabel("Veterinário Responsável:"));
			panel.add(comboVets);

			JComboBox<String> comboAnimais = new JComboBox<>(nomesAnimais);
			panel.add(new JLabel("Animal:"));
			panel.add(comboAnimais);

			JComboBox<TipoVacina> comboTipo = new JComboBox<>(TipoVacina.values());
			panel.add(new JLabel("Tipo de Vacina:"));
			panel.add(comboTipo);

			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('_');
			JFormattedTextField txtData = new JFormattedTextField(dateMask);
			panel.add(new JLabel("Data da Aplicação:"));
			panel.add(txtData);

			int result = JOptionPane.showConfirmDialog(null, panel, "Registrar Vacinação",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				return new String[]{
						String.valueOf(comboVets.getSelectedIndex()),
						String.valueOf(comboAnimais.getSelectedIndex()),
						comboTipo.getSelectedItem().toString(),
						txtData.getText()
				};
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public LoteVacina selecionarLoteDisponivel(List<LoteVacina> lotes) {
		if (lotes.isEmpty()) {
			mostraMensagem("Não há lotes ABERTOS disponíveis para este tipo de vacina.\nVá ao Estoque e abra um lote SELADO.");
			return null;
		}

		String[] descricoes = lotes.stream()
				.map(l -> "Lote: " + l.getNumeroLote() + " | Fab: " + l.getFabricante() + " | Restam: " + l.getQuantidadeDoses() + " doses")
				.toArray(String[]::new);

		String selecionado = (String) JOptionPane.showInputDialog(
				null, "Selecione o Lote Aberto:", "Seleção de Lote",
				JOptionPane.QUESTION_MESSAGE, null, descricoes, descricoes[0]);

		if (selecionado != null) {
			String numLote = selecionado.split("\\|")[0].replace("Lote: ", "").trim();
			return lotes.stream().filter(l -> l.getNumeroLote().equals(numLote)).findFirst().orElse(null);
		}
		return null;
	}

	public void mostraMensagem(String msg) {
		JOptionPane.showMessageDialog(null, msg);
	}
}
