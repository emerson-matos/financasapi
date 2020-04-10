package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class BankAccount implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;

	private String agency;
	private String number;
	private String dac;
	private Client owner;

	@OneToOne
	private Institution institution;

	public BankAccount() {
		super();
	}

	public BankAccount(final Client owner, final Institution institution, final String agency, final String number, final String dac, final Long accountId) {
		super();
		this.owner = owner;
		this.institution = institution;
		this.agency = agency;
		this.number = number;
		this.accountId = accountId;
		this.dac = dac;
	}

	public Client getOwner() {
		return owner;
	}

	public void setDono(final Client owner) {
		this.owner = owner;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(final String agency) {
		this.agency = agency;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public String getDac() {
		return dac;
	}

	public void setDac(final String dac) {
		this.dac = dac;
	}

	public Long getId() {
		return accountId;
	}

	public void setId(Long accountId) {
		this.accountId = accountId;
	}

}
