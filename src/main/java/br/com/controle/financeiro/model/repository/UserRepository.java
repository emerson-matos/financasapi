package br.com.controle.financeiro.model.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.controle.financeiro.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);
}
