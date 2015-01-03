package org.sales.medsales.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.sales.medsales.api.dominio.EntityBase;

/**
 * Parceiro pode representa um Cliente (que compra os produtos à MedSales) ou vendedor
 * (a quem MedSales compra os produtos) .
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Parceiro extends EntityBase<Long>{

	@Column(nullable=false, length=100, unique=true)
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
