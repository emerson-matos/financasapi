package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "institution")
public class Institution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_institution")
    private Long id;

    private String name;

    private String identifier;

    public Institution() {
        super();
    }

    public Institution(final Long id, final String identifier, final String name) {
        super();
        this.id = id;
        this.identifier = identifier;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long institutionId) {
        this.id = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public Institution withId(Long id) {
        this.setId(id);
        return this;
    }

}
