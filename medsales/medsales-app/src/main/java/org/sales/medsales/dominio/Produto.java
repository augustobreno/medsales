package org.sales.medsales.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.sales.medsales.api.dominio.EntityBase;

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
	@Column(nullable=false)
	private String nome;
	
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String descricao) {
		this.nome = descricao;
	}
}
