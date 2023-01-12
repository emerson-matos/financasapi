package br.com.controle.financeiro.controller.api.linkbuilder

import br.com.controle.financeiro.controller.api.BankAccountController
import br.com.controle.financeiro.model.dto.BankAccountDTO
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class BankAccountDTOResourceAssembler :
        RepresentationModelAssembler<BankAccountDTO, EntityModel<BankAccountDTO>> {
    override fun toModel(entity: BankAccountDTO): EntityModel<BankAccountDTO> {
        return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(BankAccountController::class.java)
                                        .oneBankAccount(entity.id!!)
                        )
                        .withSelfRel(),
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(BankAccountController::class.java)
                                        .allBankAccounts()
                        )
                        .withRel("bankaccounts")
        )
    }
}
