package br.com.controle.financeiro.model.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.controle.financeiro.model.entity.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

	RoleEntity findByAuthority(String username);
}
