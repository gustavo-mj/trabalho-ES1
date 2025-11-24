package br.com.clinica.presentation;

import br.com.clinica.domain.model.EstadoLote;
import br.com.clinica.domain.model.LoteVacina;
import br.com.clinica.domain.model.Role;
import br.com.clinica.domain.model.TipoVacina;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelaEstoqueVacina extends TelaCRUD {

	@Override
	protected String getTituloTela() {
		return "Gerenciamento de Estoque de Vacinas";
	}

	@Override
	public int exibirMenu(Role role) {
		List<String> opcoesList = new ArrayList<>();

		if (role == Role.ADMIN || role == Role.SECRETARIA) {
			opcoesList.add("Cadastrar Lote"); // 0
		}

		opcoesList.add("Listar por Tipo"); // 1

		if (role == Role.ADMIN || role == Role.SECRETARIA || role == Role.VETERINARIO) {
			// Veterinário também pode precisar abrir um lote para usar
			opcoesList.add("Abrir/Gerenciar Lotes"); // 2 (Novo)
		}

		if (role == Role.ADMIN) {
			opcoesList.add("Excluir Lote"); // 3
		}

		opcoesList.add("Voltar");

		String[] opcoes = opcoesList.toArray(new String[0]);

		int escolha = JOptionPane.showOptionDialog(
				null, "Escolha uma opção", getTituloTela(),
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				null, opcoes, opcoes[0]
		);

		if (escolha == JOptionPane.CLOSED_OPTION) return 99;

		String opcaoSelecionada = opcoes[escolha];

		switch (opcaoSelecionada) {
		case "Cadastrar Lote": return 0;
		case "Listar por Tipo": return 1;
		case "Abrir/Gerenciar Lotes": return 2;
		case "Excluir Lote": return 3;
		case "Voltar": return 4;
		default: return -1;
		}
	}

	// ... (pegarDados e selecionarTipoFiltro mantidos iguais ao anterior) ...
	public String[] pegarDados() {
		try {
			MaskFormatter dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('_');

			JPanel panel = new JPanel(new GridLayout(0, 1));

			panel.add(new JLabel("Número do Lote (Ex: ABC-123):"));
			JTextField txtNumero = new JTextField();
			panel.add(txtNumero);

			panel.add(new JLabel("Fabricante:"));
			JTextField txtFabricante = new JTextField();
			panel.add(txtFabricante);

			panel.add(new JLabel("Quantidade de Doses:"));
			JTextField txtQtd = new JTextField();
			panel.add(txtQtd);

			panel.add(new JLabel("Tipo da Vacina:"));
			JComboBox<TipoVacina> comboTipo = new JComboBox<>(TipoVacina.values());
			panel.add(comboTipo);

			panel.add(new JLabel("Data Fabricação (dd/MM/yyyy):"));
			JFormattedTextField txtDataFab = new JFormattedTextField(dateMask);
			panel.add(txtDataFab);

			panel.add(new JLabel("Data Validade (dd/MM/yyyy):"));
			JFormattedTextField txtDataVal = new JFormattedTextField(dateMask);
			panel.add(txtDataVal);

			int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				return new String[]{
						txtNumero.getText(),
						txtFabricante.getText(),
						txtQtd.getText(),
						comboTipo.getSelectedItem().toString(),
						txtDataFab.getText(),
						txtDataVal.getText()
				};
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TipoVacina selecionarTipoFiltro() {
		return (TipoVacina) JOptionPane.showInputDialog(
				null, "Selecione o tipo de vacina:",
				getTituloTela(), JOptionPane.QUESTION_MESSAGE, null,
				TipoVacina.values(), TipoVacina.values()[0]);
	}

	public LoteVacina selecionarLoteParaAcao(List<LoteVacina> lotes, String acao) {
		if (lotes.isEmpty()) {
			mostraMensagem("Nenhum lote disponível para " + acao + ".");
			return null;
		}
		String[] descricoes = lotes.stream()
				.map(l -> l.getId() + " | " + l.getNumeroLote() + " (" + l.getTipoVacina() + ") - Qtd: " + l.getQuantidadeDoses())
				.toArray(String[]::new);

		String selecionado = (String) JOptionPane.showInputDialog(
				null, "Selecione o lote:", "Ação: " + acao,
				JOptionPane.QUESTION_MESSAGE, null, descricoes, descricoes[0]);

		if (selecionado != null) {
			Long id = Long.parseLong(selecionado.split("\\|")[0].trim());
			return lotes.stream().filter(l -> l.getId().equals(id)).findFirst().orElse(null);
		}
		return null;
	}

	public EstadoLote escolherNovoEstado() {
		EstadoLote[] estadosPermitidos = {EstadoLote.ABERTO, EstadoLote.INVALIDO};
		return (EstadoLote) JOptionPane.showInputDialog(
				null, "Qual o novo estado do lote?", getTituloTela(),
				JOptionPane.QUESTION_MESSAGE, null, estadosPermitidos, estadosPermitidos[0]);
	}

	public Long selecionarLoteParaExclusao(List<LoteVacina> lotes) {
		if (lotes.isEmpty()) {
			mostraMensagem("Nenhum lote encontrado.");
			return null;
		}
		String[] descricoes = lotes.stream()
				.map(l -> l.getId() + " | " + l.getNumeroLote() + " | " + l.getEstado())
				.toArray(String[]::new);

		String selecionado = (String) JOptionPane.showInputDialog(
				null, "Selecione o lote para excluir:", getTituloTela(),
				JOptionPane.WARNING_MESSAGE, null, descricoes, descricoes[0]);

		if (selecionado != null) {
			String idStr = selecionado.split("\\|")[0].trim();
			return Long.parseLong(idStr);
		}
		return null;
	}

	public void mostraLista(List<LoteVacina> lotes) {
		if (lotes.isEmpty()) {
			mostraMensagem("Estoque vazio.");
			return;
		}
		StringBuilder sb = new StringBuilder("=== ESTOQUE ===\n");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		for (LoteVacina l : lotes) {
			sb.append("[").append(l.getEstado()).append("] ")
					.append(l.getTipoVacina()).append(" - Lote: ").append(l.getNumeroLote())
					.append("\nQtd: ").append(l.getQuantidadeDoses())
					.append(" | Val: ").append(l.getDataValidade().format(fmt))
					.append("\n------------------\n");
		}
		JTextArea textArea = new JTextArea(sb.toString());
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		JOptionPane.showMessageDialog(null, scrollPane, getTituloTela(), JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostraMensagem(String msg) { JOptionPane.showMessageDialog(null, msg); }
}
