package br.com.clinica.presentation;

import br.com.clinica.domain.model.Tutor;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaTutor extends TelaCRUD {

    @Override
    protected String getTituloTela() {
        return "Tela de Tutores";
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
        try {
            // Cria máscara para a data
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField campoData = new JFormattedTextField(mask);

            // Cria painel com campos
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("CPF:"));
            JTextField cpf = new JTextField();
            panel.add(cpf);
            panel.add(new JLabel("Nome:"));
            JTextField nome = new JTextField();
            panel.add(nome);
            panel.add(new JLabel("Telefone:"));
            JTextField telefone = new JTextField();
            panel.add(telefone);
            panel.add(new JLabel("CEP:"));
            JTextField cep = new JTextField();
            panel.add(cep);
            panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"));
            panel.add(campoData);

            int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                return new String[]{
                        cpf.getText(),
                        nome.getText(),
                        telefone.getText(),
                        cep.getText(),
                        campoData.getText()
                };
            } else {
                return null; // Cancelou
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] pegarDadosAlteracao(Tutor t) {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField campoData = new JFormattedTextField(mask);
            campoData.setText(t.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(new JLabel("CPF:"));
            JTextField cpf = new JTextField(t.getCpf());
            cpf.setEditable(false); // CPF não pode ser alterado
            panel.add(cpf);

            panel.add(new JLabel("Nome:"));
            JTextField nome = new JTextField(t.getNome());
            panel.add(nome);

            panel.add(new JLabel("Telefone:"));
            JTextField telefone = new JTextField(t.getTelefone());
            panel.add(telefone);

            panel.add(new JLabel("CEP:"));
            JTextField cep = new JTextField(t.getCep());
            panel.add(cep);

            panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"));
            panel.add(campoData);

            int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                return new String[]{
                    cpf.getText(),
                    nome.getText(),
                    telefone.getText(),
                    cep.getText(),
                    campoData.getText()
                };
            } else {
                return null; // Cancelou
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String seleciona() {
        return JOptionPane.showInputDialog("Digite o CPF do Tutor:");
    }

    public void mostraLista(List<Tutor> tutores) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Tutor t : tutores) {
            sb.append("CPF: ").append(t.getCpf())
            .append(", Nome: ").append(t.getNome())
            .append(", Telefone: ").append(t.getTelefone())
            .append(", CEP: ").append(t.getCep())
            .append(", Data de Nascimento: ").append(t.getDataNascimento().format(formatter))
            .append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), getTituloTela(), JOptionPane.INFORMATION_MESSAGE);
    }


    public void mostraMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
