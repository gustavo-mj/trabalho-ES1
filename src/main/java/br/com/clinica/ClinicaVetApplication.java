package br.com.clinica;

import br.com.clinica.domain.controlador.ControladorSistema;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import br.com.clinica.domain.controlador.ControladorLogin;
import br.com.clinica.domain.model.Usuario;

@SpringBootApplication
public class ClinicaVetApplication {

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(ClinicaVetApplication.class);
		builder.headless(false);
		ConfigurableApplicationContext context = builder.run(args);

		//Pega o Controlador de Login
		ControladorLogin controladorLogin = context.getBean(ControladorLogin.class);

		//Chama o método de autenticação
		Usuario usuarioLogado = controladorLogin.autenticar();

		//Verifica se o login foi bem-sucedido
		if (usuarioLogado != null) {
			//Se foi, pega o Controlador do Sistema
			ControladorSistema sistema = context.getBean(ControladorSistema.class);

			//Inicia o sistema principal, passando o usuário logado
			sistema.inicializaSistema(usuarioLogado);
		} else {
			//Se o usuário cancelou o login, encerra a aplicação
			System.out.println("Login cancelado. Encerrando o sistema.");
			System.exit(0);
		}
	}
}
