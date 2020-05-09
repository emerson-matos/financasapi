package br.com.controle.financeiro.model.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.controle.financeiro.model.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
}
