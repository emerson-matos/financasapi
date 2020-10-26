package br.com.controle.financeiro.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findAllByOwner(UserEntity owner);

    Optional<Transaction> findByIdAndOwner(UUID responsible, UserEntity owner);

}
