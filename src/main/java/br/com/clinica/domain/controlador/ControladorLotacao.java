package br.com.clinica.domain.controlador;

import br.com.clinica.domain.model.Lotacao;
import br.com.clinica.domain.model.Turno;
import br.com.clinica.presentation.TelaLotacao;

import java.util.ArrayList;
import java.util.List;

public class ControladorLotacao {
    private final ControladorSistema sistema;
    private final TelaLotacao telaLotacao;
    private final List<Lotacao> lotacoes;

    public ControladorLotacao(ControladorSistema sistema) {
        this.sistema = sistema;
        this.telaLotacao = new TelaLotacao();
        this.lotacoes = new ArrayList<>();
    }

    public Lotacao pegaLotacaoPorOcupacao(String ocupacao) {
        for (Lotacao l : lotacoes) {
            if (l.getOcupacao().equalsIgnoreCase(ocupacao)) return l;
        }
        return null;
    }

    private void cadastrarLotacao() {
        String[] dados = telaLotacao.pegarDados();
        if (dados == null) return;

        if (pegaLotacaoPorOcupacao(dados[0]) == null) {
            try {
                Turno turno = Turno.valueOf(dados[2]);
                int salario = Integer.parseInt(dados[1]);
                Lotacao l = new Lotacao(dados[0], turno, salario);
                lotacoes.add(l);
                telaLotacao.mostraMensagem("Lotação cadastrada com sucesso!");
            } catch (NumberFormatException e) {
                telaLotacao.mostraMensagem("Salário inválido!");
            } catch (IllegalArgumentException e) {
                telaLotacao.mostraMensagem("Turno inválido!");
            }
        } else {
            telaLotacao.mostraMensagem("Lotação já cadastrada!");
        }
    }

    private void alterarLotacao() {
        telaLotacao.mostraLista(lotacoes);
        String ocupacao = telaLotacao.seleciona(lotacoes);
        if (ocupacao == null) return;

        Lotacao l = pegaLotacaoPorOcupacao(ocupacao);
        if (l != null) {
            String[] novosDados = telaLotacao.pegarDadosAlteracao(l);
            if (novosDados == null) return;

            l.setOcupacao(novosDados[0]);
            l.setSalario(Integer.parseInt(novosDados[1]));
            l.setTurno(Turno.valueOf(novosDados[2]));

            telaLotacao.mostraMensagem("Lotação alterada com sucesso!");
        }
    }

    private void listarLotacoes() {
        telaLotacao.mostraLista(lotacoes);
    }

    private void excluirLotacao() {
        listarLotacoes();
        String ocupacao = telaLotacao.seleciona(lotacoes);
        if (ocupacao == null) return;

        Lotacao l = pegaLotacaoPorOcupacao(ocupacao);
        if (l != null) {
            lotacoes.remove(l);
            telaLotacao.mostraMensagem("Lotação excluída com sucesso!");
        }
    }

    private void retornar() {
        sistema.inicializaSistema();
    }

    public void exibirMenu() {
        boolean continua = true;
        while (continua) {
            int opcao = telaLotacao.exibirMenu();
            switch (opcao) {
                case 0: // Cadastrar
                    cadastrarLotacao();
                    break;
                case 1: // Alterar
                    alterarLotacao();
                    break;
                case 2: // Listar
                    listarLotacoes();
                    break;
                case 3: // Excluir
                    excluirLotacao();
                    break;
                case 4: // Voltar
                    continua = false;
                    retornar();
                    break;
                default:
                    telaLotacao.mostraMensagem("Opção inválida!");
                    break;
            }
        }
    }

    public List<Lotacao> getLotacoes() { return lotacoes; }
}
