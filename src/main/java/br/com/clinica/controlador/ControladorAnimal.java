package br.com.clinica.controlador;

import br.com.clinica.model.Animal;
import br.com.clinica.model.Sexo;
import br.com.clinica.model.Tutor;
import br.com.clinica.view.TelaAnimal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ControladorAnimal {
    private final ControladorSistema sistema;
    private final TelaAnimal telaAnimal;
    private final List<Animal> animais;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControladorAnimal(ControladorSistema sistema) {
        this.sistema = sistema;
        this.telaAnimal = new TelaAnimal();
        this.animais = new ArrayList<>();
    }

    private Animal pegaAnimalPorChip(String chip) {
        for (Animal a : animais) {
            if (a.getIdMicrochip().equals(chip)) {
                return a;
            }
        }
        return null;
    }

    private void cadastrarAnimal() {
        String[] dados = telaAnimal.pegarDados(sistema.getControladorTutor().getTutores());
        if (dados == null) return; // Cancelou

        if (pegaAnimalPorChip(dados[0]) == null) {
            try {
                LocalDate dataNascimento = LocalDate.parse(dados[4], formatter);
                int tutorIndex = Integer.parseInt(dados[5]);
                Tutor tutorSelecionado = sistema.getControladorTutor().getTutores().get(tutorIndex);

                Animal animal = new Animal(
                        dados[0],
                        dados[1],
                        dados[2],
                        Sexo.valueOf(dados[3].toUpperCase()),
                        dataNascimento,
                        tutorSelecionado
                );

                animais.add(animal);
                telaAnimal.mostraMensagem("Animal cadastrado com sucesso!");
            } catch (DateTimeParseException e) {
                telaAnimal.mostraMensagem("Data inválida! Use o formato dd/MM/yyyy.");
            } catch (IndexOutOfBoundsException e) {
                telaAnimal.mostraMensagem("Tutor inválido! Cadastro não realizado.");
            } catch (IllegalArgumentException e) {
                telaAnimal.mostraMensagem("Sexo inválido! Cadastro não realizado.");
            }
        } else {
            telaAnimal.mostraMensagem("Animal já cadastrado!");
        }
    }

    private void alterarCadastro() {
        listarAnimais();
        String chip = telaAnimal.seleciona(animais);
        if (chip == null) return; // Cancelou

        Animal a = pegaAnimalPorChip(chip);
        if (a != null) {
            String[] novosDados = telaAnimal.pegarDadosAlteracao(a, sistema.getControladorTutor().getTutores());
            if (novosDados == null) return; // Cancelou

            a.setNome(novosDados[1]);
            a.setEspecie(novosDados[2]);

            try {
                a.setSexo(Sexo.valueOf(novosDados[3].toUpperCase()));
            } catch (IllegalArgumentException e) {
                telaAnimal.mostraMensagem("Sexo inválido! Alteração não realizada.");
                return;
            }

            try {
                a.setDataNascimento(LocalDate.parse(novosDados[4], formatter));
            } catch (DateTimeParseException e) {
                telaAnimal.mostraMensagem("Data inválida! Alteração não realizada.");
                return;
            }

            try {
                int tutorIndex = Integer.parseInt(novosDados[5]);
                Tutor tutorSelecionado = sistema.getControladorTutor().getTutores().get(tutorIndex);
                a.setTutor(tutorSelecionado);
            } catch (IndexOutOfBoundsException e) {
                telaAnimal.mostraMensagem("Tutor inválido! Alteração não realizada.");
                return;
            }

            telaAnimal.mostraMensagem("Cadastro alterado com sucesso!");
        } else {
            telaAnimal.mostraMensagem("Animal não cadastrado.");
        }
    }

    private void listarAnimais() {
        telaAnimal.mostraLista(animais);
    }

    private void excluirAnimal() {
        listarAnimais();
        String chip = telaAnimal.seleciona(animais);
        if (chip == null) return; // Cancelou

        Animal a = pegaAnimalPorChip(chip);
        if (a != null) {
            animais.remove(a);
            telaAnimal.mostraMensagem("Animal excluído com sucesso!");
        } else {
            telaAnimal.mostraMensagem("Animal não cadastrado.");
        }
    }

    private void retornar() {
        sistema.inicializaSistema(); // ou abreTelaPrincipal se preferir
    }

    public void exibirMenu() {
        boolean continua = true;
        while (continua) {
            int opcao = telaAnimal.exibirMenu();
            switch (opcao) {
                case 0:
                    cadastrarAnimal();
                    break;
                case 1:
                    alterarCadastro();
                    break;
                case 2:
                    listarAnimais();
                    break;
                case 3:
                    excluirAnimal();
                    break;
                case 4:
                    continua = false;
                    retornar();
                    break;
                default:
                    telaAnimal.mostraMensagem("Opção inválida!");
                    break;
            }
        }
    }

    // Método getter para testes ou uso externo
    public List<Animal> getAnimais() {
        return animais;
    }
}
