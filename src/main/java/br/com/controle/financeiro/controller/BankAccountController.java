package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.BankAccountResourceAssembler;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.exception.BankAccountNotFoundException;
import br.com.controle.financeiro.model.repository.BankAccountRepository;

@RestController
@RequestMapping("/bankaccount")
public class BankAccountController {

	private static final Logger LOG = LoggerFactory.getLogger(BankAccountController.class);

	private final BankAccountRepository bankAccountRepository;

	private final BankAccountResourceAssembler bankAccountResourceAssembler;

	public BankAccountController(final BankAccountRepository bankAccountRepository,
			final BankAccountResourceAssembler bankAccountResourceAssembler) {
		this.bankAccountRepository = bankAccountRepository;
		this.bankAccountResourceAssembler = bankAccountResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<BankAccount>> allBankAccounts() {
		LOG.debug("finding allBankAccounts");

		final List<Resource<BankAccount>> bankAccounts = bankAccountRepository.findAll().stream()
				.map(bankAccountResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(bankAccounts, linkTo(methodOn(BankAccountController.class).allBankAccounts()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Resource<BankAccount> newBankAccount(@RequestBody final BankAccount bankAccount) throws URISyntaxException {
		LOG.debug("creating newBankAccount");
		return bankAccountResourceAssembler.toResource(bankAccountRepository.save(bankAccount));
	}

	@GetMapping(path = "/{id}")
	public Resource<BankAccount> oneBankAccount(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneBankAccount ${}", id);
		final BankAccount c = bankAccountRepository.findById(id).orElseThrow(() -> new BankAccountNotFoundException(id));
		return bankAccountResourceAssembler.toResource(c);
	}

	@PutMapping(path = "/{id}")
	public BankAccount replaceBankAccount(@RequestBody final BankAccount newBankAccount, @PathVariable final Long id) {
		LOG.info("replaceBankAccount");
		return bankAccountRepository.findById(id).map(bankAccount -> {
			bankAccount.setAgency(newBankAccount.getAgency());
			bankAccount.setDac(newBankAccount.getDac());
			bankAccount.setNumber(newBankAccount.getNumber());
			return bankAccountRepository.save(bankAccount);
		}).orElseGet(() -> {
			newBankAccount.setId(id);
			return bankAccountRepository.save(newBankAccount);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteBankAccount(@PathVariable final Long id) {
		LOG.debug("trying to deleteBankAccount ${}", id);
		bankAccountRepository.deleteById(id);
	}
}
