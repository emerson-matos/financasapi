package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class BankAccountDTO implements Serializable {

	private Long accountId;

	private String agency;
	private String number;
	private String dac;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long ownerId;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long institutionId;

	@JsonProperty(access = Access.READ_ONLY)
	private Client owner;

	@JsonProperty(access = Access.READ_ONLY)
	private Institution institution;

	public BankAccountDTO() {
		super();
	}

	public BankAccountDTO(final String agency, final String number, final String dac, final Long owner,
			final Long institution) {
		super();
		this.agency = agency;
		this.number = number;
		this.dac = dac;
		this.ownerId = owner;
		this.institutionId = institution;
	}

	public BankAccountDTO(Long id, String agency, String number, String dac, Client owner, Institution institution) {
		super();
		this.agency = agency;
		this.accountId = id;
		this.number = number;
		this.dac = dac;
		this.owner = owner;
		this.institution = institution;
	}

	public static BankAccountDTO fromBankAccount(BankAccount account) {
		return new BankAccountDTO(account.getId(), account.getAgency(), account.getNumber(), account.getDac(),
				account.getOwner(), account.getInstitution());
	}

	public BankAccount toBankAccount() {
		Client client = new Client().withId(this.ownerId);
		Institution instObj = new Institution().withId(institutionId);
		return new BankAccount(client, instObj, this.agency, this.number, this.dac, this.accountId);
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

	public Long getOwnerId() {
		return this.ownerId;
	}

	public void setOwner(final Long owner) {
		this.ownerId = owner;
	}

	public Long getInstitutionId() {
		return this.institutionId;
	}

	public void setInstitution(final Long institutionId) {
		this.institutionId = institutionId;
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
