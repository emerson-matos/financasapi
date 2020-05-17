package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class CardDTO implements Serializable {

	private Long cardId;

	private String name;
	private String number;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long ownerId;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long institutionId;

	@JsonProperty(access = Access.READ_ONLY)
	private Client owner;

	@JsonProperty(access = Access.READ_ONLY)
	private Institution institution;


	public CardDTO(){
		super();
	}

	public CardDTO(final String name, final String number, final Long ownerId, final Long institutionId) {
		super();
		this.name = name;
		this.number = number;
		this.ownerId = ownerId;
		this.institutionId = institutionId;
	}

	private CardDTO(final Long id, final String name, final String number, final Client owner, final Institution institution) {
		super();
		this.cardId = id;
		this.name = name;
		this.number = number;
		this.owner = owner;
		this.institution = institution;
	}

	public static CardDTO fromCard(final Card card) {
		return new CardDTO(card.getId(), card.getName(), card.getNumber(), card.getOwner(), card.getInstitution());
	}

	public Card toCard() {
		Client client = new Client().withId(this.ownerId);
		Institution instObj = new Institution().withId(this.institutionId);
		return new Card(this.cardId, this.name, this.number, client, instObj);
	}

	public Long getId() {
		return this.cardId;
	}

	public void setId(final Long id) {
		this.cardId = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public Client getOwner() {
		return this.owner;
	}

	public void setOwner(final Client client) {
		this.owner = client;
	}

	public Institution getInstitution() {
		return this.institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public Long getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(final Long owner) {
		this.ownerId = owner;
	}

	public Long getInstitutionId() {
		return this.institutionId;
	}

	public void setInstitutionId(final Long id) {
		this.institutionId = id;
	}
}
