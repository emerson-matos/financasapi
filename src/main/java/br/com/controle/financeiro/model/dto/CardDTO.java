package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class CardDTO implements Serializable {

	private Long cardId;

	private String name;
	private String number;

	private Long owner;

	private Long institution;

	public CardDTO() {
		super();
	}

	public CardDTO(final String name, final String number, final Long owner, final Long institution) {
		super();
		this.name = name;
		this.number = number;
		this.owner = owner;
		this.institution = institution;
	}

	private CardDTO(final Long id, final String name, final String number, final Long owner, final Long institution) {
		super();
		this.cardId = id;
		this.name = name;
		this.number = number;
		this.owner = owner;
		this.institution = institution;
	}

	public static CardDTO fromCard(final Card card) {
		return new CardDTO(card.getId(), card.getName(), card.getNumber(), card.getOwner().getId(), card.getInstitution().getId());
	}

	public Card toCard() {
		Client client = new Client().withId(this.owner);
		Institution instObj = new Institution().withId(this.institution);
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

	public Long getOwner() {
		return this.owner;
	}

	public void setOwner(final Long owner) {
		this.owner = owner;
	}

	public Long getInstitution() {
		return this.institution;
	}

	public void setName(final Long institution) {
		this.institution = institution;
	}
}
