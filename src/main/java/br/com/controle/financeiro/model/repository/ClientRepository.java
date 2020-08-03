package br.com.controle.financeiro.model.repository;

import java.util.UUID;

import br.com.controle.financeiro.model.entity.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

}
