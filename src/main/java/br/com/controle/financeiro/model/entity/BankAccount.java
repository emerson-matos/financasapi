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

@Entity(name = "bank_account")
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_account")
    private Long id;

    private String agency;
    private String number;
    private String dac;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client responsible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_institution")
    private Institution institution;

    public BankAccount() {
        super();
    }

    public BankAccount(final Long id, final String agency, final String number, final String dac,
                       final Client responsible, final Institution institution) {
        super();
        this.id = id;
        this.agency = agency;
        this.number = number;
        this.dac = dac;
        this.responsible = responsible;
        this.institution = institution;
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

    public Long getId() {
        return id;
    }

    public void setId(Long accountId) {
        this.id = accountId;
    }

    public BankAccount withId(Long id) {
        this.setId(id);
        return this;
    }

}
