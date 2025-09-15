package br.com.clinica.model;

public class Lotacao {
    private String ocupacao;
    private Turno turno;
    private int salario;

    public Lotacao(String ocupacao, Turno turno, int salario) {
        this.ocupacao = ocupacao;
        this.turno = turno;
        this.salario = salario;
    }

    public String getOcupacao() { return ocupacao; }

    public void setOcupacao(String ocupacao) { this.ocupacao = ocupacao; }

    public Turno getTurno() { return turno; }

    public void setTurno(Turno turno) { this.turno = turno; }

    public int getSalario() { return salario;}

    public void setSalario(int salario) { this.salario = salario; }

}