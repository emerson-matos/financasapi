package br.com.controle.financeiro.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.controle.financeiro.model.entity.Client;

@org.springframework.stereotype.Repository
public interface ClienteRepository extends JpaRepository<Client, Long> {
}
