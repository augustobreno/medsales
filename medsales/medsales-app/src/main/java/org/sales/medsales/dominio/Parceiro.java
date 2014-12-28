package org.sales.medsales.dominio;

import javax.persistence.Column;
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

	@Column(nullable=false)
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
