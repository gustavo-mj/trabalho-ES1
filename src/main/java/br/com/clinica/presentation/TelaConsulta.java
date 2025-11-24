package br.com.clinica.presentation;

import br.com.clinica.domain.model.Consulta;
import br.com.clinica.domain.model.Role;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelaConsulta extends TelaCRUD {

	@Override
	protected String getTituloTela() {
		return "Gestão de Consultas Médicas";
	}

	@Override
	public int exibirMenu(Role role) {
		List<String> opcoesList = new ArrayList<>();

		// VETERINARIO é quem cadastra/altera consulta
		// ADMIN também pode para corrigir erros
		if (role == Role.VETERINARIO || role == Role.ADMIN) {
			opcoesList.add("Registrar Consulta"); // 0
			opcoesList.add("Alterar Consulta");   // 1
		}

		opcoesList.add("Listar Consultas"); // Index varia

		if (role == Role.ADMIN) {
			opcoesList.add("Excluir Consulta"); // Index varia
		}

		opcoesList.add("Voltar");

		String[] opcoes = opcoesList.toArray(new String[0]);
		int escolha = JOptionPane.showOptionDialog(
				null, "Escolha uma opção", getTituloTela(),
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, opcoes, opcoes[0]);

		if (escolha == JOptionPane.CLOSED_OPTION) return 99;

		String op = opcoes[escolha];
		switch (op) {
		case "Registrar Consulta": return 0;
		case "Alterar Consulta": return 1;
		case "Listar Consultas": return 2;
		case "Excluir Consulta": return 3;
		case "Voltar": return 4;
		default: return -1;
		}
	}

	// Formulário para Cadastro
	public String[] pegarDados(String[] nomesVets, String[] nomesAnimais) {
		return criarFormulario(nomesVets, nomesAnimais, null);
	}

	// Formulário para Alteração (vem preenchido)
	public String[] pegarDadosAlteracao(Consulta c, String[] nomesVets, String[] nomesAnimais, int idxVet, int idxAni) {
		return criarFormulario(nomesVets, nomesAnimais, c);
	}

	private String[] criarFormulario(String[] nomesVets, String[] nomesAnimais, Consulta c) {
		try {
			JPanel panel = new JPanel(new GridLayout(0, 1)); // Layout vertical

			// ComboBoxes
			JComboBox<String> comboVets = new JComboBox<>(nomesVets);
			JComboBox<String> comboAnimais = new JComboBox<>(nomesAnimais);

			// Se for alteração, seleciona os atuais
			if (c != null) {
				// Lógica de seleção será feita no controller ou passada por indice,
				// mas aqui deixamos padrão ou setamos se tivermos os indices.
				// (Para simplificar a view, vamos confiar que o user seleciona,
				// ou implementaremos a seleção exata no controller antes de chamar).
			}

			panel.add(new JLabel("Veterinário:"));
			panel.add(comboVets);
			panel.add(new JLabel("Animal:"));
			panel.add(comboAnimais);

			// Data
			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('_');
			JFormattedTextField txtData = new JFormattedTextField(dateMask);
			if (c != null) txtData.setText(c.getDataConsulta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			panel.add(new JLabel("Data (dd/MM/yyyy):"));
			panel.add(txtData);

			// Campos de Texto
			JTextField txtMotivo = new JTextField(c != null ? c.getMotivo() : "");
			panel.add(new JLabel("Motivo/Queixa:"));
			panel.add(txtMotivo);

			JTextField txtDiag = new JTextField(c != null ? c.getDiagnostico() : "");
			panel.add(new JLabel("Diagnóstico:"));
			panel.add(txtDiag);

			JTextField txtTrat = new JTextField(c != null ? c.getTratamento() : "");
			panel.add(new JLabel("Tratamento:"));
			panel.add(txtTrat);

			int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				return new String[]{
						String.valueOf(comboVets.getSelectedIndex()),
						String.valueOf(comboAnimais.getSelectedIndex()),
						txtData.getText(),
						txtMotivo.getText(),
						txtDiag.getText(),
						txtTrat.getText()
				};
			}
		} catch (ParseException e) { e.printStackTrace(); }
		return null;
	}

	public Long selecionarConsulta(List<Consulta> consultas) {
		if (consultas.isEmpty()) {
			mostraMensagem("Nenhuma consulta encontrada.");
			return null;
		}
		String[] itens = consultas.stream()
				.map(c -> "ID: " + c.getId() + " | Data: " + c.getDataConsulta() + " | Animal: " + c.getAnimal().getNome())
				.toArray(String[]::new);

		String selecionado = (String) JOptionPane.showInputDialog(
				null, "Selecione a consulta:", getTituloTela(),
				JOptionPane.QUESTION_MESSAGE, null, itens, itens[0]);

		if (selecionado != null) {
			String idStr = selecionado.split("\\|")[0].replace("ID:", "").trim();
			return Long.parseLong(idStr);
		}
		return null;
	}

	public void mostraLista(List<Consulta> consultas) {
		if (consultas.isEmpty()) { mostraMensagem("Lista vazia."); return; }

		StringBuilder sb = new StringBuilder("=== REGISTRO DE CONSULTAS ===\n\n");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (Consulta c : consultas) {
			sb.append("ID: ").append(c.getId())
					.append(" | Data: ").append(c.getDataConsulta().format(fmt))
					.append("\nVet: ").append(c.getVeterinario().getNome())
					.append(" | Animal: ").append(c.getAnimal().getNome())
					.append("\nMotivo: ").append(c.getMotivo())
					.append("\nDiagnóstico: ").append(c.getDiagnostico())
					.append("\nTratamento: ").append(c.getTratamento())
					.append("\n-----------------------------------\n");
		}

		JTextArea area = new JTextArea(sb.toString());
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area);
		scroll.setPreferredSize(new Dimension(400, 400));
		JOptionPane.showMessageDialog(null, scroll, getTituloTela(), JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostraMensagem(String msg) { JOptionPane.showMessageDialog(null, msg); }
}
