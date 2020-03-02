package br.com.controle.financeiro.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.controle.financeiro.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
