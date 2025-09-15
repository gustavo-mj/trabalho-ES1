package br.com.clinica.abstrato;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class TelaCRUD {
    protected JFrame frame;
    protected int opcaoSelecionada = 0;

    // Cada subclasse define o título da tela
    protected abstract String getTituloTela();

    // Exibe o menu de CRUD genérico
    public int exibirMenu() {
        frame = new JFrame(getTituloTela());
        frame.setSize(350, 220);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Título
        JLabel titulo = new JLabel(getTituloTela(), JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titulo, BorderLayout.NORTH);

        // Painel de opções
        JPanel panelOpcoes = new JPanel();
        panelOpcoes.setLayout(new BoxLayout(panelOpcoes, BoxLayout.Y_AXIS));

        JRadioButton cadastrar = new JRadioButton("Cadastrar");
        JRadioButton listar = new JRadioButton("Listar");
        JRadioButton excluir = new JRadioButton("Excluir");
        JRadioButton voltar = new JRadioButton("Voltar");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(cadastrar);
        grupo.add(listar);
        grupo.add(excluir);
        grupo.add(voltar);

        panelOpcoes.add(cadastrar);
        panelOpcoes.add(listar);
        panelOpcoes.add(excluir);
        panelOpcoes.add(voltar);

        frame.add(panelOpcoes, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton confirmar = new JButton("Confirmar");
        JButton cancelar = new JButton("Cancelar");

        confirmar.addActionListener((ActionEvent e) -> {
            if (cadastrar.isSelected()) opcaoSelecionada = 1;
            else if (listar.isSelected()) opcaoSelecionada = 2;
            else if (excluir.isSelected()) opcaoSelecionada = 3;
            else if (voltar.isSelected()) opcaoSelecionada = 0;

            frame.dispose();
        });

        cancelar.addActionListener((ActionEvent e) -> {
            opcaoSelecionada = 0;
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
}
