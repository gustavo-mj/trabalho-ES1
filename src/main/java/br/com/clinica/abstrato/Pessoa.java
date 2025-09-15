package br.com.clinica.abstrato;

import java.time.LocalDate;

public class Pessoa {
    private String cpf;
    private String nome;
    private String telefone;
    private String cep;
    private LocalDate dataNascimento;

    public Pessoa(String cpf, String nome, String telefone, String cep, LocalDate dataNascimento) {
        this.cpf = cpf;
        this.nome = nome;
        this.telefone = telefone;
        this.cep = cep;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCep() { return cep; }

    public void setCep(String cep) { this.cep = cep; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

}