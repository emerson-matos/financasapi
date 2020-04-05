package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.BankAccountController;
import br.com.controle.financeiro.model.entity.BankAccount;

@Component
public class BankAccountResourceAssembler implements ResourceAssembler<BankAccount, Resource<BankAccount>> {

	@Override
	public Resource<BankAccount> toResource(BankAccount entity) {
		return new Resource<>(entity, linkTo(methodOn(BankAccountController.class).oneBankAccount(entity.getId())).withSelfRel(),
				linkTo(methodOn(BankAccountController.class).allBankAccounts()).withRel("clients"));
	}

}
