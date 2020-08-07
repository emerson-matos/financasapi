package br.com.controle.financeiro.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    List<Card> findAllByOwner(UserEntity owner);

    Optional<Card> findByIdAndOwner(UUID responsible, UserEntity owner);

}
