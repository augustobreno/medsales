package org.sales.medsales.dominio;

import javax.persistence.Entity;

/**
 * Entidade que representa os dados de um produto comercializado. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Produto extends EntityBase<Long> {

	/**
	 * Código de barras cadastrado.
	 */
	private String codigoBarras;
	
	/**
	 * Descrição do produto.
	 */
	private String descricao;
	
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
