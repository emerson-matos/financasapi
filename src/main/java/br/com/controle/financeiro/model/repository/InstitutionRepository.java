package br.com.controle.financeiro.model.repository;

import java.util.UUID;

import br.com.controle.financeiro.model.entity.Institution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID> {

}
