package br.com.clinica.controlador;

import br.com.clinica.view.TelaMenu;

public class ControladorSistema {
    // Singleton
    private static ControladorSistema instance = null;

    // Controladores de entidades
    private final ControladorAnimal controladorAnimal;
    private final ControladorTutor controladorTutor;
    private final ControladorProfissional controladorProfissional;
    private final ControladorLotacao controladorLotacao;

    // Tela principal
    private final TelaMenu telaMenu;

    // Construtor privado para singleton
    private ControladorSistema() {
        this.controladorLotacao = new ControladorLotacao(this);
        this.controladorAnimal = new ControladorAnimal(this);
        this.controladorTutor = new ControladorTutor(this);
        this.controladorProfissional = new ControladorProfissional(this);
        this.telaMenu = new TelaMenu();
    }

    // Retorna a instância singleton
    public static ControladorSistema getInstance() {
        if (instance == null) {
            instance = new ControladorSistema();
        }
        return instance;
    }

    // Getters públicos para os controladores
    public ControladorAnimal getControladorAnimal() { return controladorAnimal; }
    public ControladorTutor getControladorTutor() { return controladorTutor; }
    public ControladorProfissional getControladorProfissional() { return controladorProfissional; }
    public ControladorLotacao getControladorLotacao() { return controladorLotacao; }

    // Inicializa o sistema
    public void inicializaSistema() {
        abreMenuPrincipal();
    }

    // Loop do menu principal
    private void abreMenuPrincipal() {
        while (true) {
            int opcao = telaMenu.exibirMenu();

            switch (opcao) {
                case 1:
                    abreTelaProfissionais();
                    break;
                case 2:
                    abreTelaTutores();
                    break;
                case 3:
                    abreTelaAnimais();
                    break;
                case 4:
                    abreTelaLotacoes();
                    break;
                case 0:
                    encerraSistema();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    // Métodos para abrir telas de entidades
    private void abreTelaAnimais() { controladorAnimal.exibirMenu(); }
    private void abreTelaTutores() { controladorTutor.exibirMenu(); }
    private void abreTelaProfissionais() { controladorProfissional.exibirMenu(); }
    private void abreTelaLotacoes() { controladorLotacao.exibirMenu(); }

    // Encerra o sistema
    private void encerraSistema() {
        System.out.println("Encerrando o sistema...");
        System.exit(0);
    }
}
