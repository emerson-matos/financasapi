package br.com.controle.financeiro.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    List<BankAccount> findAllByOwner(UserEntity owner);

    Optional<BankAccount> findByIdAndOwner(UUID id, UserEntity owner);
}
