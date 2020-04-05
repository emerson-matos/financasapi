package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.InstitutionController;
import br.com.controle.financeiro.model.entity.Institution;

@Component
public class InstitutionResourceAssembler implements ResourceAssembler<Institution, Resource<Institution>> {

	@Override
	public Resource<Institution> toResource(Institution entity) {
		return new Resource<>(entity, linkTo(methodOn(InstitutionController.class).oneInstitution(entity.getId())).withSelfRel(),
				linkTo(methodOn(InstitutionController.class).allInstitutions()).withRel("institution"));
	}

}
