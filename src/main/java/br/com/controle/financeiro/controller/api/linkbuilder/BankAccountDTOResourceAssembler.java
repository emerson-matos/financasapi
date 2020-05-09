package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.api.BankAccountController;
import br.com.controle.financeiro.model.dto.BankAccountDTO;

@Component
public class BankAccountDTOResourceAssembler implements ResourceAssembler<BankAccountDTO, Resource<BankAccountDTO>> {

	@Override
	public Resource<BankAccountDTO> toResource(BankAccountDTO entity) {
		return new Resource<>(entity,
				linkTo(methodOn(BankAccountController.class).oneBankAccount(entity.getId())).withSelfRel(),
				linkTo(methodOn(BankAccountController.class).allBankAccounts()).withRel("bankaccounts"));
	}

}
