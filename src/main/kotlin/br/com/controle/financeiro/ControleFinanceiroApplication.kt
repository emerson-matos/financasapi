package br.com.controle.financeiro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan("br.com.controle.financeiro.model.entity")
@EnableJpaRepositories("br.com.controle.financeiro.model.repository")
class ControleFinanceiroApplication

fun main(args: Array<String>) {
    runApplication<ControleFinanceiroApplication>(*args)
}
