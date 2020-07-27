package br.com.controle.financeiro.model.repository;

import br.com.controle.financeiro.model.entity.Role;

import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByAuthority(String username);

}
