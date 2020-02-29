package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idResponsavel;

	private String nome;
	
	public Client() {
		super();
	}
	
	public Client(String nome) {
		super();
		this.nome = nome;
	}
	
	public Long getId() {
		return this.idResponsavel;
	}

	public void setId(Long id) {
		this.idResponsavel = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
