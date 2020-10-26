package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.BankAccountController;
import br.com.controle.financeiro.model.dto.BankAccountDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class BankAccountDTOResourceAssembler implements RepresentationModelAssembler<BankAccountDTO, EntityModel<BankAccountDTO>> {

    @Override
    public EntityModel<BankAccountDTO> toModel(BankAccountDTO entity) {
        return new EntityModel<>(entity, linkTo(methodOn(BankAccountController.class).oneBankAccount(entity.getId()))
                .withSelfRel(),
                              linkTo(methodOn(BankAccountController.class).allBankAccounts()).withRel("bankaccounts"));
    }

}
