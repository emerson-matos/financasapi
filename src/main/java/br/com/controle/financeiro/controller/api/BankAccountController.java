package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.controller.api.linkbuilder.BankAccountDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.BankAccountDTO;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.exception.BankAccountNotFoundException;
import br.com.controle.financeiro.model.repository.BankAccountRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.InstitutionRepository;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/bankaccount", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class BankAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountController.class);

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountDTOResourceAssembler bankAccountDTOResourceAssembler;

    private final ClientRepository clientRepository;

    private final InstitutionRepository institutionRepository;

    private final UserService userService;

    public BankAccountController(final BankAccountRepository bankAccountRepository,
                                 final BankAccountDTOResourceAssembler bankAccountDTOResourceAssembler,
                                 final ClientRepository clientRepository,
                                 final InstitutionRepository institutionRepository, final UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountDTOResourceAssembler = bankAccountDTOResourceAssembler;
        this.clientRepository = clientRepository;
        this.institutionRepository = institutionRepository;
        this.userService = userService;
    }

    @GetMapping
    public Resources<Resource<BankAccountDTO>> allBankAccounts() {
        LOG.debug("finding user BankAccounts");
        UserEntity owner = userService.getAuthenticatedUser();
        final List<Resource<BankAccountDTO>> bankAccounts =
                bankAccountRepository.findAllByOwner(owner).stream().map(BankAccountDTO::fromBankAccount)
                                     .map(bankAccountDTOResourceAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(bankAccounts,
                               linkTo(methodOn(BankAccountController.class).allBankAccounts()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public Resource<BankAccountDTO> newBankAccount(@RequestBody @Valid BankAccountDTO bankAccount) {
        LOG.debug("creating newBankAccount");
        //TODO extract to service
        UserEntity owner = userService.getAuthenticatedUser();
        Client client = clientRepository.findByIdAndOwner(bankAccount.getResponsible(), owner).orElseThrow();
        Institution institution = institutionRepository.findById(bankAccount.getInstitution()).orElseThrow();
        BankAccountDTO savedBankAccountDTO = BankAccountDTO
                .fromBankAccount(bankAccountRepository.save(bankAccount.toBankAccount(client, institution, owner)));

        return bankAccountDTOResourceAssembler.toResource(savedBankAccountDTO);
    }

    @GetMapping(path = "/{id}")
    public Resource<BankAccountDTO> oneBankAccount(@PathVariable(value = "id") final UUID id) {
        LOG.debug("searching oneBankAccount ${}", id);
        UserEntity owner = userService.getAuthenticatedUser();
        final BankAccount account = bankAccountRepository.findByIdAndOwner(id, owner)
                                                         .orElseThrow(() -> new BankAccountNotFoundException(id));
        return bankAccountDTOResourceAssembler.toResource(BankAccountDTO.fromBankAccount(account));
    }

    @PutMapping(path = "/{id}")
    public BankAccountDTO replaceBankAccount(@RequestBody final BankAccountDTO newBankAccountDTO,
                                             @PathVariable final UUID id) {
        LOG.info("replaceBankAccount");
        //TODO verify DTO integrity
        final UserEntity owner = userService.getAuthenticatedUser();
        BankAccount savedAccount = bankAccountRepository.findByIdAndOwner(id, owner).map(bankAccount -> {
            bankAccount.setAgency(newBankAccountDTO.getAgency());
            bankAccount.setDac(newBankAccountDTO.getDac());
            bankAccount.setNumber(newBankAccountDTO.getNumber());
            return bankAccountRepository.save(bankAccount);
        }).orElseGet(() -> {
            //TODO extract to service
            Client client = clientRepository.findByIdAndOwner(newBankAccountDTO.getResponsible(), owner).orElseThrow();
            Institution institution = institutionRepository.findById(newBankAccountDTO.getInstitution()).orElseThrow();
            newBankAccountDTO.setId(id);
            return bankAccountRepository.save(newBankAccountDTO.toBankAccount(client, institution, owner));
        });

        return BankAccountDTO.fromBankAccount(savedAccount);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteBankAccount(@PathVariable final UUID id) {
        LOG.debug("trying to deleteBankAccount ${}", id);
        //TODO verify authenticated permission
        bankAccountRepository.deleteById(id);
    }

}
