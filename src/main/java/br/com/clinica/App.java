package br.com.clinica;

import br.com.clinica.controlador.ControladorSistema;

public class App {
    public static void main(String[] args) {
        ControladorSistema sistema = ControladorSistema.getInstance();
        sistema.inicializaSistema();
    }
}