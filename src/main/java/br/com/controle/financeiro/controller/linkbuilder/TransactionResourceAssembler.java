package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.TransactionController;
import br.com.controle.financeiro.model.entity.Transaction;

@Component
public class TransactionResourceAssembler implements ResourceAssembler<Transaction, Resource<Transaction>> {

	@Override
	public Resource<Transaction> toResource(Transaction entity) {
		return new Resource<>(entity, linkTo(methodOn(TransactionController.class).oneTransaction(entity.getId())).withSelfRel(),
				linkTo(methodOn(TransactionController.class).allTransactions()).withRel("transaction"));
	}

}
