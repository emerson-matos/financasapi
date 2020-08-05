package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "card")
public class Card extends AbstractPersistable<UUID> implements Serializable {

    private String name;
    private String number;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client responsible;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_institution")
    private Institution institution;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_user")
    private UserEntity owner;

    public Card() {
        super();
    }

    public Card(final UUID id, final String name, final String number, final Client responsible,
                final Institution institution, UserEntity owner) {
        super();
        this.setId(id);
        this.name = name;
        this.number = number;
        this.responsible = responsible;
        this.institution = institution;
        this.owner = owner;
    }

    public Client getResponsible() {
        return responsible;
    }

    public void setResponsible(final Client owner) {
        this.responsible = owner;
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

}
