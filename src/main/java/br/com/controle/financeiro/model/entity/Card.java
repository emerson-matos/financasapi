package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Card implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cardId;

	private String name;
	private String number;

	@OneToOne
	private Client owner;

	@OneToOne
	private Institution institution;

	public Card() {
		super();
	}

	public Card(final String name, final String number, final Client owner, final Institution institution) {
		super();
		this.name = name;
		this.number = number;
		this.owner = owner;
		this.institution = institution;
	}

	public Client getOwner() {
		return owner;
	}

	public void setOwner(final Client owner) {
		this.owner = owner;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstituicao(final Institution institution) {
		this.institution = institution;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getId() {
		return cardId;
	}

	public void setId(Long cardId) {
		this.cardId = cardId;
	}

}
