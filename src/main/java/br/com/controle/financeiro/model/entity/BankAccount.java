package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "bank_account")
public class BankAccount extends AbstractPersistable<UUID> implements Serializable {

    private String agency;
    private String number;
    private String dac;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client responsible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_institution")
    private Institution institution;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_user")
    private UserEntity owner;

    public BankAccount() {
        super();
    }

    public BankAccount(final UUID id, final String agency, final String number, final String dac,
                       final Client responsible, final Institution institution, UserEntity owner) {
        super();
        this.setId(id);
        this.agency = agency;
        this.number = number;
        this.dac = dac;
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

}
