package br.com.clinica.domain.model;

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