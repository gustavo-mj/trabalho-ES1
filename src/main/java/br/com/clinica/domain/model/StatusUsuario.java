package br.com.clinica.domain.model;

public enum StatusUsuario {
	NOVO,       // Primeiro acesso, exige troca de senha
	ATIVO,      // Uso normal
	BLOQUEADO   // Excedeu tentativas
}
