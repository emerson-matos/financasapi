package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.api.TransactionController;
import br.com.controle.financeiro.model.dto.TransactionDTO;

@Component
public class TransactionDTOResourceAssembler implements ResourceAssembler<TransactionDTO, Resource<TransactionDTO>> {

	@Override
	public Resource<TransactionDTO> toResource(TransactionDTO entity) {
		return new Resource<>(entity, linkTo(methodOn(TransactionController.class).oneTransaction(entity.getId())).withSelfRel(),
				linkTo(methodOn(TransactionController.class).allTransactions()).withRel("transactions"));
	}

}
