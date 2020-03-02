package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.ClientController;
import br.com.controle.financeiro.model.entity.Client;

@Component
public class ClientResourceAssembler implements ResourceAssembler<Client, Resource<Client>> {

	@Override
	public Resource<Client> toResource(Client entity) {
		return new Resource<>(entity, linkTo(methodOn(ClientController.class).oneClient(entity.getId())).withSelfRel(),
				linkTo(methodOn(ClientController.class).allClients()).withRel("clients"));
	}

}
