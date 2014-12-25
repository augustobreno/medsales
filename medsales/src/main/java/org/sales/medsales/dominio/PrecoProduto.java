package org.sales.medsales.dominio;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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
	@ManyToOne(fetch=FetchType.LAZY)
	private Produto produto;
	
	/**
	 * Valor comercializado .
	 */
	@Column(precision=19, scale=2)
	private BigDecimal valor;
	
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getValidoEm() {
		return validoEm;
	}

	public void setValidoEm(Date validoEm) {
		this.validoEm = validoEm;
	}
	
}
