package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

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

import br.com.controle.financeiro.controller.linkbuilder.BankAccountDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.BankAccountDTO;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.exception.BankAccountNotFoundException;
import br.com.controle.financeiro.model.repository.BankAccountRepository;

@RestController
@RequestMapping("/bankaccount")
public class BankAccountController {

	private static final Logger LOG = LoggerFactory.getLogger(BankAccountController.class);

	private final BankAccountRepository bankAccountRepository;

	private final BankAccountDTOResourceAssembler bankAccountDTOResourceAssembler;

	public BankAccountController(final BankAccountRepository bankAccountRepository,
			final BankAccountDTOResourceAssembler bankAccountDTOResourceAssembler) {
		this.bankAccountRepository = bankAccountRepository;
		this.bankAccountDTOResourceAssembler = bankAccountDTOResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<BankAccountDTO>> allBankAccounts() {
		LOG.debug("finding allBankAccounts");

		final List<Resource<BankAccountDTO>> bankAccounts = bankAccountRepository.findAll().stream()
				.map(BankAccountDTO::fromBankAccount).map(bankAccountDTOResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(bankAccounts,
				linkTo(methodOn(BankAccountController.class).allBankAccounts()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
	public Resource<BankAccountDTO> newBankAccount(@RequestBody final BankAccountDTO bankAccount) {
		LOG.debug("creating newBankAccount");

		BankAccountDTO savedBankAccountDTO = BankAccountDTO
				.fromBankAccount(bankAccountRepository.save(bankAccount.toBankAccount()));
		return bankAccountDTOResourceAssembler.toResource(savedBankAccountDTO);
	}

	@GetMapping(path = "/{id}")
	public Resource<BankAccountDTO> oneBankAccount(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneBankAccount ${}", id);
		final BankAccount account = bankAccountRepository.findById(id)
				.orElseThrow(() -> new BankAccountNotFoundException(id));
		return bankAccountDTOResourceAssembler.toResource(BankAccountDTO.fromBankAccount(account));
	}

	@PutMapping(path = "/{id}")
	public BankAccountDTO replaceBankAccount(@RequestBody final BankAccountDTO newBankAccountDTO,
			@PathVariable final Long id) {
		LOG.info("replaceBankAccount");
		//TODO verify DTO integrity
		BankAccount savedAccount = bankAccountRepository.findById(id).map(bankAccount -> {
			bankAccount.setAgency(newBankAccountDTO.getAgency());
			bankAccount.setDac(newBankAccountDTO.getDac());
			bankAccount.setNumber(newBankAccountDTO.getNumber());
			return bankAccountRepository.save(bankAccount);
		}).orElseGet(() -> {
			newBankAccountDTO.setId(id);
			return bankAccountRepository.save(newBankAccountDTO.toBankAccount());
		});

		return BankAccountDTO.fromBankAccount(savedAccount);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteBankAccount(@PathVariable final Long id) {
		LOG.debug("trying to deleteBankAccount ${}", id);
		bankAccountRepository.deleteById(id);
	}
}
