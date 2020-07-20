package br.com.controle.financeiro.model.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.controle.financeiro.model.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByAuthority(String username);
}
