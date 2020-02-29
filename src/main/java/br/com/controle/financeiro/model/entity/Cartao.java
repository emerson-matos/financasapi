package br.com.controle.financeiro.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Cartao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idCartao;

	private String name;
	private String numero;
	
	@OneToOne
	private Client dono;

	@OneToOne
	private Instituicao instituicao;
	
	public Cartao() {
		super();
	}
	
	public Cartao(String nome, String numero, Client dono, Instituicao instituicao) {
		super();
		this.setName(nome);
		this.numero = numero;
		this.dono = dono;
		this.instituicao = instituicao;
	}
	
	public Client getDono() {
		return dono;
	}

	public void setDono(Client dono) {
		this.dono = dono;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
