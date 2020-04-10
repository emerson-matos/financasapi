package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class BankAccountDTO implements Serializable {

	private Long accountId;

	private String agency;
	private String number;
	private String dac;
	private Client owner;

	private Institution institution;

	public BankAccountDTO() {
		super();
	}

	public BankAccountDTO(final String agency, final String number, final String dac, final Client owner, final Institution institution) {
		super();
		this.agency = agency;
		this.number = number;
		this.dac = dac;
		this.owner = owner;
		this.institution = institution;
	}

	private BankAccountDTO(final Long id, final String agency, final String number, final String dac,
			final Client owner, final Institution institution) {
		super();
		this.agency = agency;
		this.accountId = id;
		this.number = number;
		this.dac = dac;
		this.owner = owner;
		this.institution = institution;
	}

	public static BankAccountDTO fromBankAccount(BankAccount client) {
		return new BankAccountDTO(client.getId(), client.getAgency(), client.getNumber(), client.getDac(),
				client.getOwner(), client.getInstitution());
	}

	public BankAccount toBankAccount() {
		return new BankAccount(this.owner, this.institution, this.agency, this.number, this.dac, this.accountId);
	}

	public Long getId() {
		return this.accountId;
	}

	public void setId(final Long id) {
		this.accountId = id;
	}

	public String getAgency() {
		return this.agency;
	}

	public void setAgency(final String agency) {
		this.agency = agency;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public String getDac() {
		return this.dac;
	}

	public void setDac(final String dac) {
		this.dac = dac;
	}

	public Client getOwner() {
		return this.owner;
	}

	public void setOwner(final Client owner) {
		this.owner = owner;
	}

	public Institution getInstitution() {
		return this.institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

}
