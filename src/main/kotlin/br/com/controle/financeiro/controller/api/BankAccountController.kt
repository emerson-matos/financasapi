package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.controller.api.BankAccountController
import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler
import br.com.controle.financeiro.model.dto.BankAccountDTO
import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.exception.BankAccountNotFoundException
import br.com.controle.financeiro.model.repository.BankAccountRepository
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.InstitutionRepository
import br.com.controle.financeiro.model.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import jakarta.validation.Valid
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/bankaccount"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BankAccountController(
    private val bankAccountRepository: BankAccountRepository,
    private val bankAccountDTOResourceAssembler: BankAccountDTOResourceAssembler,
    private val clientRepository: ClientRepository,
    private val institutionRepository: InstitutionRepository,
    private val userRepository: UserRepository,
) {
    @GetMapping
    fun allBankAccounts(): CollectionModel<EntityModel<BankAccountDTO>> {
        LOG.debug("finding user BankAccounts")
        val owner = userRepository.findAll().iterator().next()
        val bankAccounts = bankAccountRepository.findAllByOwner(owner).stream().map { account: BankAccount ->
            BankAccountDTO.fromBankAccount(
                account
            )
        }
            .map { entity: BankAccountDTO -> bankAccountDTOResourceAssembler.toModel(entity) }
        .toList()
        return CollectionModel.of(
            bankAccounts,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BankAccountController::class.java).allBankAccounts())
                .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newBankAccount(@RequestBody bankAccount: @Valid BankAccountDTO): EntityModel<BankAccountDTO> {
        LOG.debug("creating newBankAccount")
        //TODO extract to service
        val owner = userRepository.findAll().iterator().next()
        val client = clientRepository.findByIdAndOwner(bankAccount.responsible.id, owner).orElseThrow()
        val institution = bankAccount.institution.id.let { institutionRepository.findById(it).orElseThrow() }
        val savedBankAccountDTO: BankAccountDTO = BankAccountDTO.fromBankAccount(
            bankAccountRepository.save(
                bankAccount.toBankAccount(client, institution, owner)
            )
        )
        return bankAccountDTOResourceAssembler.toModel(savedBankAccountDTO)
    }

    @GetMapping(path = ["/{id}"])
    fun oneBankAccount(@PathVariable(value = "id") id: UUID): EntityModel<BankAccountDTO> {
        LOG.debug("searching oneBankAccount \${}", id)
        val owner = userRepository.findAll().iterator().next()
        val account = bankAccountRepository.findByIdAndOwner(id, owner)
            .orElseThrow { BankAccountNotFoundException(id) }
        return bankAccountDTOResourceAssembler.toModel(BankAccountDTO.fromBankAccount(account))
    }

    // @PutMapping(path = ["/{id}"])
    // fun replaceBankAccount(
        // @RequestBody newBankAccountDTO: BankAccountDTO,
        // @PathVariable id: UUID
    // ): BankAccountDTO {
        // LOG.info("replaceBankAccount")
        //TODO verify DTO integrity
        // val owner = userRepository.findAll().iterator().next()
        // val savedAccount = bankAccountRepository.findByIdAndOwner(id, owner).map { bankAccount: BankAccount ->
            // bankAccount.agency = newBankAccountDTO.agency
            // bankAccount.dac = newBankAccountDTO.dac
            // bankAccount.number = newBankAccountDTO.number
            // bankAccountRepository.save(bankAccount)
        // }.orElseGet {

            // TODO extract to service
            // val client: Client = clientRepository.findByIdAndOwner(newBankAccountDTO.responsible.id, owner).orElseThrow()
            // val institution = newBankAccountDTO.institution.id.let { institutionRepository.findById(it).orElseThrow() }
            // newBankAccountDTO.id = id
            // bankAccountRepository.save(newBankAccountDTO.toBankAccount(client, institution, owner))
        // }
        // return BankAccountDTO.fromBankAccount(savedAccount)
    // }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteBankAccount(@PathVariable id: UUID) {
        LOG.debug("trying to deleteBankAccount \${}", id)
        //TODO verify authenticated permission
        bankAccountRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BankAccountController::class.java)
    }
}
