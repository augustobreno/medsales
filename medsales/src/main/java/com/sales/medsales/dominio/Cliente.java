package com.sales.medsales.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Aquele que compra os produtos à MedSales.
 * @author Augusto
 */
@Entity
public class Cliente extends EntityBase<Long> {

	@Column(nullable=false, length=100, unique=true)
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
