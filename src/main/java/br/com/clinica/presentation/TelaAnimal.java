package br.com.clinica.presentation;

import br.com.clinica.domain.model.Animal;
import br.com.clinica.domain.model.Tutor;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaAnimal extends TelaCRUD {

    @Override
    protected String getTituloTela() {
        return "Tela de Animais";
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

    public String[] pegarDados(List<Tutor> tutores) {
        if (tutores.isEmpty()) {
            mostraMensagem("Não existem tutores cadastrados. Cadastre um tutor antes de cadastrar um animal.");
            return null;
        }

        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField campoData = new JFormattedTextField(mask);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(new JLabel("ID Microchip:"));
            JTextField idMicrochip = new JTextField();
            panel.add(idMicrochip);

            panel.add(new JLabel("Nome:"));
            JTextField nome = new JTextField();
            panel.add(nome);

            panel.add(new JLabel("Espécie:"));
            JTextField especie = new JTextField();
            panel.add(especie);

            String[] opcoesSexo = {"MASCULINO", "FEMININO"};
            JComboBox<String> comboSexo = new JComboBox<>(opcoesSexo);
            panel.add(new JLabel("Sexo:"));
            panel.add(comboSexo);

            panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"));
            panel.add(campoData);

            // Lista de tutores disponíveis
            String[] nomesTutors = tutores.stream().map(Tutor::getNome).toArray(String[]::new);
            JComboBox<String> comboTutor = new JComboBox<>(nomesTutors);
            panel.add(new JLabel("Tutor:"));
            panel.add(comboTutor);

            int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                int tutorIndex = comboTutor.getSelectedIndex();
                return new String[]{
                        idMicrochip.getText(),
                        nome.getText(),
                        especie.getText(),
                        (String) comboSexo.getSelectedItem(), // retorna "M" ou "F"
                        campoData.getText(),
                        String.valueOf(tutorIndex) // índice do tutor
                };
            } else {
                return null; // Cancelou
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] pegarDadosAlteracao(Animal a, List<Tutor> tutores) {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField campoData = new JFormattedTextField(mask);
            campoData.setText(a.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(new JLabel("ID Microchip:"));
            JTextField idMicrochip = new JTextField(a.getIdMicrochip());
            idMicrochip.setEditable(false);
            panel.add(idMicrochip);

            panel.add(new JLabel("Nome:"));
            JTextField nome = new JTextField(a.getNome());
            panel.add(nome);

            panel.add(new JLabel("Espécie:"));
            JTextField especie = new JTextField(a.getEspecie());
            panel.add(especie);

            panel.add(new JLabel("Sexo:"));
            JComboBox<String> comboSexo = new JComboBox<>(new String[]{"M", "F"});
            comboSexo.setSelectedItem(a.getSexo().name());
            panel.add(comboSexo);

            panel.add(new JLabel("Data de Nascimento (dd/MM/yyyy):"));
            panel.add(campoData);

            // Tutor
            String[] nomesTutors = tutores.stream().map(Tutor::getNome).toArray(String[]::new);
            JComboBox<String> comboTutor = new JComboBox<>(nomesTutors);
            comboTutor.setSelectedIndex(tutores.indexOf(a.getTutor()));
            panel.add(new JLabel("Tutor:"));
            panel.add(comboTutor);

            int result = JOptionPane.showConfirmDialog(null, panel, getTituloTela(),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                int tutorIndex = comboTutor.getSelectedIndex();
                return new String[]{
                        idMicrochip.getText(),
                        nome.getText(),
                        especie.getText(),
                        (String) comboSexo.getSelectedItem(), // retorna "M" ou "F"
                        campoData.getText(),
                        String.valueOf(tutorIndex)
                };
            } else {
                return null;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String seleciona(List<Animal> animais) {
        if (animais.isEmpty()) {
            mostraMensagem("Não existem animais cadastrados.");
            return null;
        }

        String[] ids = animais.stream().map(Animal::getIdMicrochip).toArray(String[]::new);
        return (String) JOptionPane.showInputDialog(null, "Selecione o animal pelo Microchip:", getTituloTela(),
                JOptionPane.PLAIN_MESSAGE, null, ids, ids[0]);
    }

    public void mostraLista(List<Animal> animais) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Animal a : animais) {
            sb.append("ID: ").append(a.getIdMicrochip())
                    .append(", Nome: ").append(a.getNome())
                    .append(", Espécie: ").append(a.getEspecie())
                    .append(", Sexo: ").append(a.getSexo())
                    .append(", Data Nascimento: ").append(a.getDataNascimento().format(formatter))
                    .append(", Tutor: ").append(a.getTutor().getNome())
                    .append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), getTituloTela(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
