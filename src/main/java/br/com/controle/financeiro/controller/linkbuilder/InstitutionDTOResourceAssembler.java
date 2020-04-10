package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.InstitutionController;
import br.com.controle.financeiro.model.dto.InstitutionDTO;

@Component
public class InstitutionDTOResourceAssembler implements ResourceAssembler<InstitutionDTO, Resource<InstitutionDTO>> {

	@Override
	public Resource<InstitutionDTO> toResource(InstitutionDTO entity) {
		return new Resource<>(entity, linkTo(methodOn(InstitutionController.class).oneInstitution(entity.getId())).withSelfRel(),
				linkTo(methodOn(InstitutionController.class).allInstitutions()).withRel("institution"));
	}

}
