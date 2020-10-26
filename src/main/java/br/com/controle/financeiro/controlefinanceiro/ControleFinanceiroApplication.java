package br.com.controle.financeiro.controlefinanceiro;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableSwagger2
@ComponentScan({ "br.com.controle.financeiro" })
@EntityScan({ "br.com.controle.financeiro.model.entity" })
@EnableJpaRepositories({ "br.com.controle.financeiro.model.repository" })
public class ControleFinanceiroApplication extends SpringBootServletInitializer {

    public static void main(String... args) {
        SpringApplication app = new SpringApplication(ControleFinanceiroApplication.class);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ControleFinanceiroApplication.class);
    }

}
