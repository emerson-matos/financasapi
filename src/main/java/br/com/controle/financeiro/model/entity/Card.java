package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "card")
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_card")
    private Long id;

    private String name;
    private String number;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client owner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_institution")
    private Institution institution;

    public Card() {
        super();
    }

    public Card(final Long id, final String name, final String number, final Client owner,
                final Institution institution) {
        super();
        this.id = id;
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
        return id;
    }

    public void setId(Long cardId) {
        this.id = cardId;
    }

    public Card withId(Long id) {
        this.setId(id);
        return this;
    }

}
