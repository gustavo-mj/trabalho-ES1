package br.com.clinica.domain.controlador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.clinica.domain.model.Tutor;
import br.com.clinica.presentation.TelaTutor;

public class ControladorTutor {
    private final ControladorSistema sistema;
    private final TelaTutor telaTutor;
    private final List<Tutor> tutores;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControladorTutor(ControladorSistema sistema) {
        this.sistema = sistema;
        this.telaTutor = new TelaTutor();
        this.tutores = new ArrayList<>();
    }

    public List<Tutor> getTutores() {
        return tutores;
    }

    public Tutor pegaTutorPorCpf(String cpf) {
        for (Tutor t : tutores) {
            if (t.getCpf().equals(cpf)) {
                return t;
            }
        }
        return null;
    }

    private void cadastrarTutor() {
        String[] dados = telaTutor.pegarDados();
        if (dados == null) return; // Cancelou

        if (pegaTutorPorCpf(dados[0]) == null) {
            try {
                LocalDate dataNascimento = LocalDate.parse(dados[4], formatter);
                Tutor tutor = new Tutor(dados[0], dados[1], dados[2], dados[3], dataNascimento);
                tutores.add(tutor);
                telaTutor.mostraMensagem("Tutor cadastrado com sucesso!");
            } catch (DateTimeParseException e) {
                telaTutor.mostraMensagem("Data inválida! Use o formato dd/MM/yyyy.");
            }
        } else {
            telaTutor.mostraMensagem("ATENÇÃO: Tutor já cadastrado!");
        }
    }

    private void alterarCadastro() {
        listarTutores();
        String cpf = telaTutor.seleciona();
        if (cpf == null) return; // Cancelou

        Tutor t = pegaTutorPorCpf(cpf);
        if (t != null) {
            String[] novosDados = telaTutor.pegarDadosAlteracao(t);
            if (novosDados == null) return; // Cancelou
            t.setNome(novosDados[1]);
            t.setTelefone(novosDados[2]);
            t.setCep(novosDados[3]);
            try {
                LocalDate dataNascimento = LocalDate.parse(novosDados[4], formatter);
                t.setDataNascimento(dataNascimento);
            } catch (DateTimeParseException e) {
                telaTutor.mostraMensagem("Data inválida! Alteração não realizada.");
                return;
            }
            telaTutor.mostraMensagem("Cadastro alterado com sucesso!");
        } else {
            telaTutor.mostraMensagem("ATENÇÃO: Tutor não cadastrado.");
        }
    }

    private void listarTutores() {
        telaTutor.mostraLista(tutores);
    }

    private void excluirTutor() {
        listarTutores();
        String cpf = telaTutor.seleciona();
        if (cpf == null) return; // Cancelou

        Tutor t = pegaTutorPorCpf(cpf);
        if (t != null) {
            tutores.remove(t);
            telaTutor.mostraMensagem("Tutor excluído com sucesso!");
        } else {
            telaTutor.mostraMensagem("ATENÇÃO: Tutor não cadastrado.");
        }
    }

    private void retornar() {
        sistema.inicializaSistema();
    }

    public void exibirMenu() {
        boolean continua = true;
        while (continua) {
            int opcao = telaTutor.exibirMenu();
            switch (opcao) {
                case 0:
                    cadastrarTutor();
                    break;
                case 1:
                    alterarCadastro();
                    break;
                case 2:
                    listarTutores();
                    break;
                case 3:
                    excluirTutor();
                    break;
                case 4:
                    continua = false;
                    retornar();
                    break;
                default:
                    telaTutor.mostraMensagem("Opção inválida!");
                    break;
            }
        }
    }
}
