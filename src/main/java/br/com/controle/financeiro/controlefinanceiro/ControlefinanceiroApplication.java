package br.com.controle.financeiro.controlefinanceiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import br.com.controle.financeiro.controller.ClienteController;

@SpringBootApplication
@ComponentScan({"br.com.controle.financeiro"})
@EntityScan({"br.com.controle.financeiro.model"})
@EnableJpaRepositories({"br.com.controle.financeiro.model"})
public class ControlefinanceiroApplication extends SpringBootServletInitializer{

	public static void main(String... args) {
		SpringApplication.run(ControlefinanceiroApplication.class, args);
	}

}