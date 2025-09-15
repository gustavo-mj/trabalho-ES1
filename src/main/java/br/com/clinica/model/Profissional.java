package br.com.clinica.model;

import br.com.clinica.abstrato.Pessoa;
import java.time.LocalDate;

public class Profissional extends Pessoa {

    private Lotacao lotacao;

    public Profissional(String cpf, String nome, String telefone, String cep, LocalDate dataNascimento, Lotacao lotacao) {
        super(cpf, nome, telefone, cep, dataNascimento);
        this.lotacao = lotacao;
    }

    public Lotacao getLotacao() { return lotacao; }

    public void setLotacao(Lotacao lotacao) { this.lotacao = lotacao; }

}