package br.com.clinica.model;

import java.time.LocalDate;

public class Animal {
    private String idMicroChip;
    private String nome;
    private String especie;
    private Sexo sexo;
    private LocalDate dataNascimento;
    private Tutor tutor;

    public Animal(String idMicroChip, String nome, String especie, Sexo sexo, LocalDate dataNascimento, Tutor tutor) {
        this.idMicroChip = idMicroChip;
        this.nome = nome;
        this.especie = especie;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.tutor = tutor;
    }

    public String getIdMicrochip() { return idMicroChip; }

    public void setIdMicroChip(String idMicroChip) { this.idMicroChip = idMicroChip; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getEspecie() { return especie; }

    public void setEspecie(String especie) { this.especie = especie; }

    public Sexo getSexo() { return sexo; }

    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Tutor getTutor() { return tutor; }

    public void setTutor(Tutor tutor) { this.tutor = tutor;}
}