package br.com.controle.financeiro.model.repository;

import java.util.List;
import java.util.UUID;

import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    List<Client> findAllByOwner(UserEntity owner);

}
