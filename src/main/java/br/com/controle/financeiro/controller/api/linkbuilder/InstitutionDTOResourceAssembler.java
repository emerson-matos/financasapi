package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.InstitutionController;
import br.com.controle.financeiro.model.dto.InstitutionDTO;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class InstitutionDTOResourceAssembler implements ResourceAssembler<InstitutionDTO, Resource<InstitutionDTO>> {

    @Override
    public Resource<InstitutionDTO> toResource(InstitutionDTO entity) {
        return new Resource<>(entity, linkTo(methodOn(InstitutionController.class).oneInstitution(entity.getId()))
                .withSelfRel(),
                              linkTo(methodOn(InstitutionController.class).allInstitutions()).withRel("institutions"));
    }

}
