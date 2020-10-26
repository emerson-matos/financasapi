package br.com.controle.financeiro.model.repository;

import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, String> {

}
