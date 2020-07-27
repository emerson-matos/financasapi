package br.com.controle.financeiro.model.repository;

import java.util.Optional;

import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

}
