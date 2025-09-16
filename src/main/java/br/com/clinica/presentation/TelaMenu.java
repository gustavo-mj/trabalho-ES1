package br.com.clinica.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaMenu {
    private JFrame frame;
    private int opcaoSelecionada = 0;

    public int exibirMenu() {
        frame = new JFrame("Sistema da Clínica Veterinária");
        frame.setSize(400, 300); // aumentei um pouco para caber a nova opção
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Bem-vindo ao Sistema!", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titulo, BorderLayout.NORTH);

        // Painel de opções
        JPanel panelOpcoes = new JPanel();
        panelOpcoes.setLayout(new BoxLayout(panelOpcoes, BoxLayout.Y_AXIS));

        JRadioButton profissionais = new JRadioButton("Profissionais");
        JRadioButton tutores = new JRadioButton("Tutores");
        JRadioButton animais = new JRadioButton("Animais");
        JRadioButton lotacoes = new JRadioButton("Lotação");
        JRadioButton sair = new JRadioButton("Sair");

        // Agrupa para permitir apenas uma seleção
        ButtonGroup grupoOpcoes = new ButtonGroup();
        grupoOpcoes.add(profissionais);
        grupoOpcoes.add(tutores);
        grupoOpcoes.add(animais);
        grupoOpcoes.add(lotacoes);
        grupoOpcoes.add(sair);

        panelOpcoes.add(profissionais);
        panelOpcoes.add(tutores);
        panelOpcoes.add(animais);
        panelOpcoes.add(lotacoes);
        panelOpcoes.add(sair);

        frame.add(panelOpcoes, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");

        confirmar.addActionListener((ActionEvent e) -> {
            if (profissionais.isSelected()) opcaoSelecionada = 1;
            else if (tutores.isSelected()) opcaoSelecionada = 2;
            else if (animais.isSelected()) opcaoSelecionada = 3;
            else if (lotacoes.isSelected()) opcaoSelecionada = 4;
            else if (sair.isSelected()) opcaoSelecionada = 0;

            frame.dispose(); // fecha a janela
        });

        cancelar.addActionListener((ActionEvent e) -> {
            opcaoSelecionada = 0;
            frame.dispose();
        });

        panelBotoes.add(confirmar);
        panelBotoes.add(cancelar);

        frame.add(panelBotoes, BorderLayout.SOUTH);

        // Centraliza e exibe
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Espera até a janela ser fechada
        while (frame.isDisplayable()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        return opcaoSelecionada;
    }
}
