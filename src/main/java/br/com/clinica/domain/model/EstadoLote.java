package br.com.clinica.domain.model;

public enum EstadoLote {
	SELADO,     // Chegou, está fechado
	ABERTO,     // Está em uso
	ESGOTADO,   // Acabou as doses
	INVALIDO    // Quebrou, venceu, etc.
}
