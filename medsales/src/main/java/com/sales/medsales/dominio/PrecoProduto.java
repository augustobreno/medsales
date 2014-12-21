package com.sales.medsales.dominio;

import java.util.Date;

import javax.persistence.Entity;

/**
 * O preço de um produto pode variar com o tempo. Esta entidade 
 * permite isolar os dados de um produto do histórico do seu preço.
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class PrecoProduto extends EntityBase<Long> {

	/**
	 * Dados do produto assiciado.
	 */
	private Produto produto;
	
	/**
	 * Valor comercializado .
	 */
	private Double valor;
	
	/**
	 * Determina a data de aferição do preço (geralmente a data
	 * de importação ou cadastro de valores).
	 */
	private Date validoEm;

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getValidoEm() {
		return validoEm;
	}

	public void setValidoEm(Date validoEm) {
		this.validoEm = validoEm;
	}
	
}
