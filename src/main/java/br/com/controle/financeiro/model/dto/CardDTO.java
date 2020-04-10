package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class CardDTO implements Serializable {

	private Long cardId;

	private String name;
	private String number;

	private Client owner;

	private Institution institution;

	public CardDTO() {
		super();
	}

	public CardDTO(final String name, final String number, final Client owner, final Institution institution) {
		super();
		this.name = name;
		this.number = number;
		this.owner = owner;
		this.institution = institution;
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
		return new Card(this.cardId, this.name, this.number, this.owner, this.institution);
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

	public void setOwner(final Client owner) {
		this.owner = owner;
	}

	public Institution getInstitution() {
		return this.institution;
	}

	public void setName(final Institution institution) {
		this.institution = institution;
	}
}
