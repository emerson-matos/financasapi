package br.com.controle.financeiro.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Instituicao {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idInstituicao;
	private String nome;	
	private String identificador;

	public Instituicao() {
		super();
	}
	public Instituicao(String nome) {
		super();
		this.nome = nome;
	}
	public Long getIdInstituicao() {
		return idInstituicao;
	}
	public void setIdInstituicao(Long idInstituicao) {
		this.idInstituicao = idInstituicao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
}
