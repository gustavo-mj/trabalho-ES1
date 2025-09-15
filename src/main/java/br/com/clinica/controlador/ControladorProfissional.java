package br.com.clinica.controlador;

import br.com.clinica.model.Lotacao;
import br.com.clinica.model.Profissional;
import br.com.clinica.view.TelaProfissional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ControladorProfissional {

    private final ControladorSistema sistema;
    private final TelaProfissional telaProfissional;
    private final List<Profissional> profissionais;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ControladorProfissional(ControladorSistema sistema) {
        this.sistema = sistema;
        this.telaProfissional = new TelaProfissional();
        this.profissionais = new ArrayList<>();
    }

    public Profissional pegaProfissionalPorCpf(String cpf) {
        for (Profissional p : profissionais) {
            if (p.getCpf().equals(cpf)) return p;
        }
        return null;
    }

    private void cadastrarProfissional() {
        if (sistema.getControladorLotacao().getLotacoes().isEmpty()) {
            telaProfissional.mostraMensagem("Não existem lotações cadastradas. Cadastre uma lotação primeiro.");
            return;
        }

        String[] dados = telaProfissional.pegarDados(sistema.getControladorLotacao().getLotacoes());
        if (dados == null) return;

        try {
            Lotacao lotacao = sistema.getControladorLotacao().getLotacoes().get(Integer.parseInt(dados[5]));
            LocalDate data = LocalDate.parse(dados[4], formatter);

            Profissional p = new Profissional(dados[0], dados[1], dados[2], dados[3], data, lotacao);
            profissionais.add(p);
            telaProfissional.mostraMensagem("Profissional cadastrado com sucesso!");

        } catch (DateTimeParseException e) {
            telaProfissional.mostraMensagem("Data de nascimento inválida!");
        } catch (IndexOutOfBoundsException e) {
            telaProfissional.mostraMensagem("Lotação inválida!");
        }
    }

    private void alterarProfissional() {
        telaProfissional.mostraLista(profissionais);
        String cpf = telaProfissional.seleciona(profissionais);
        if (cpf == null) return;

        Profissional p = pegaProfissionalPorCpf(cpf);
        if (p != null) {
            String[] dados = telaProfissional.pegarDadosAlteracao(p, sistema.getControladorLotacao().getLotacoes());
            if (dados == null) return;

            try {
                p.setNome(dados[1]);
                p.setTelefone(dados[2]);
                p.setCep(dados[3]);
                p.setDataNascimento(LocalDate.parse(dados[4], formatter));
                p.setLotacao(sistema.getControladorLotacao().getLotacoes().get(Integer.parseInt(dados[5])));

                telaProfissional.mostraMensagem("Profissional alterado com sucesso!");
            } catch (DateTimeParseException e) {
                telaProfissional.mostraMensagem("Data de nascimento inválida!");
            } catch (IndexOutOfBoundsException e) {
                telaProfissional.mostraMensagem("Lotação inválida!");
            }
        } else {
            telaProfissional.mostraMensagem("Profissional não cadastrado.");
        }
    }

    private void listarProfissionais() {
        telaProfissional.mostraLista(profissionais);
    }

    private void excluirProfissional() {
        listarProfissionais();
        String cpf = telaProfissional.seleciona(profissionais);
        if (cpf == null) return;

        Profissional p = pegaProfissionalPorCpf(cpf);
        if (p != null) {
            profissionais.remove(p);
            telaProfissional.mostraMensagem("Profissional excluído com sucesso!");
        } else {
            telaProfissional.mostraMensagem("Profissional não cadastrado.");
        }
    }

    private void retornar() {
        sistema.inicializaSistema();
    }

    public void exibirMenu() {
        boolean continua = true;
        while (continua) {
            int opcao = telaProfissional.exibirMenu();
            switch (opcao) {
                case 0: // Cadastrar
                    cadastrarProfissional();
                    break;
                case 1: // Alterar
                    alterarProfissional();
                    break;
                case 2: // Listar
                    listarProfissionais();
                    break;
                case 3: // Excluir
                    excluirProfissional();
                    break;
                case 4: // Voltar
                    continua = false;
                    retornar();
                    break;
                default:
                    telaProfissional.mostraMensagem("Opção inválida!");
                    break;
            }
        }
    }

    public List<Profissional> getProfissionais() {
        return profissionais;
    }
}
