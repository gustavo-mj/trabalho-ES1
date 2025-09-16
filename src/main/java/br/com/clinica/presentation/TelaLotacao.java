package br.com.clinica.presentation;

import br.com.clinica.domain.model.Lotacao;

import javax.swing.*;
import java.util.List;

public class TelaLotacao extends TelaCRUD {

    @Override
    protected String getTituloTela() {
        return "Tela de Lotações";
    }

    @Override
    public int exibirMenu() {
        String[] opcoes = {"Cadastrar", "Alterar", "Listar", "Excluir", "Voltar"};
        return JOptionPane.showOptionDialog(
                null,
                "Escolha uma opção",
                getTituloTela(),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
    }

    public String[] pegarDados() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Ocupação:"));
        JTextField ocupacao = new JTextField();
        panel.add(ocupacao);

        panel.add(new JLabel("Salário:"));
        JTextField salario = new JTextField();
        panel.add(salario);

        String[] opcoesTurno = {"MATUTINO", "VESPERTINO", "PLANTAO_NOTURNO"};
        JComboBox<String> comboTurno = new JComboBox<>(opcoesTurno);
        panel.add(new JLabel("Turno:"));
        panel.add(comboTurno);

        int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return new String[]{
                    ocupacao.getText(),
                    salario.getText(),
                    comboTurno.getSelectedItem().toString()
            };
        } else {
            return null;
        }
    }

    public String[] pegarDadosAlteracao(Lotacao l) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Ocupação:"));
        JTextField ocupacao = new JTextField(l.getOcupacao());
        panel.add(ocupacao);

        panel.add(new JLabel("Salário:"));
        JTextField salario = new JTextField(String.valueOf(l.getSalario()));
        panel.add(salario);

        String[] opcoesTurno = {"MATUTINO", "VESPERTINO", "PLANTAO_NOTURNO"};
        JComboBox<String> comboTurno = new JComboBox<>(opcoesTurno);
        comboTurno.setSelectedItem(l.getTurno().name());
        panel.add(new JLabel("Turno:"));
        panel.add(comboTurno);

        int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            return new String[]{
                    ocupacao.getText(),
                    salario.getText(),
                    comboTurno.getSelectedItem().toString()
            };
        } else {
            return null;
        }
    }

    public String seleciona(List<Lotacao> lotacoes) {
        if (lotacoes.isEmpty()) {
            mostraMensagem("Não existem lotações cadastradas.");
            return null;
        }

        String[] nomes = lotacoes.stream().map(Lotacao::getOcupacao).toArray(String[]::new);
        return (String) JOptionPane.showInputDialog(null, "Selecione a lotação:", getTituloTela(),
                JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);
    }

    public void mostraLista(List<Lotacao> lotacoes) {
        StringBuilder sb = new StringBuilder();
        for (Lotacao l : lotacoes) {
            sb.append("Ocupação: ").append(l.getOcupacao())
                    .append(", Salário: ").append(l.getSalario())
                    .append(", Turno: ").append(l.getTurno())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), getTituloTela(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
