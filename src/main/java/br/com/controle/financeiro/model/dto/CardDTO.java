package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;

public class CardDTO implements Serializable {

    private Long cardId;

    @NotBlank
    private String name;

    @NotBlank
    private String number;

    @NotNull
    private UUID client;

    @NotNull
    private Long institution;

    public CardDTO() {
        super();
    }

    private CardDTO(final Long id, final String name, final String number, final UUID client, final Long institution) {
        super();
        this.cardId = id;
        this.name = name;
        this.number = number;
        this.client = client;
        this.institution = institution;
    }

    public static CardDTO fromCard(final Card card) {
        return new CardDTO(card.getId(), card.getName(), card.getNumber(), card.getOwner().getId(),
                           card.getInstitution().getId());
    }

    public Card toCard(Client client, Institution institution) {
        return new Card(this.cardId, this.name, this.number, client, institution);
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

    public UUID getClient() {
        return this.client;
    }

    public void setClient(final UUID client) {
        this.client = client;
    }

    public Long getInstitution() {
        return this.institution;
    }

    public void setInstitution(final Long institution) {
        this.institution = institution;
    }

}
