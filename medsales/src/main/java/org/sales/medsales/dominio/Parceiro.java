package org.sales.medsales.dominio;

import javax.persistence.Entity;

/**
 * Um Parceiro é uma pessoa/estabelecimento onde são realizadas as 
 * aquisições (compras).
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Entity
public class Parceiro extends EntityBase<Long> {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
