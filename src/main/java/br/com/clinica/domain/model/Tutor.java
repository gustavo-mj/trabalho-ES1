package br.com.clinica.domain.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tutor extends Pessoa {

    private List<Animal> animais = new ArrayList<>();

    public Tutor(String cpf, String nome, String telefone, String cep, LocalDate dataNascimento) {
        super(cpf, nome, telefone, cep, dataNascimento);
    }

    public List<Animal> getAnimais() { return animais; }

    public void setAnimais(List<Animal> animais) { this.animais = animais; }

    public void adicionarAnimal(Animal animal) { animais.add(animal); }

}