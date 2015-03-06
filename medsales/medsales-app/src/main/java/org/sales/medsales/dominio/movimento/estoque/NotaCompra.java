package org.sales.medsales.dominio.movimento.estoque;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.sales.medsales.api.dominio.EntityBase;

/**
 * Para anotação dos dados das notas originais de compras no fornecedor.
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class NotaCompra extends EntityBase<Long> {

	/**
	 * Saida de estoque associada.
	 */
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private SaidaEstoque saidaEstoque;
	
	/**
	 * Valor comercializado .
	 */
	@Column(precision=19, scale=2, nullable=false)
	private BigDecimal valor;

	public SaidaEstoque getSaidaEstoque() {
		return saidaEstoque;
	}

	public void setSaidaEstoque(SaidaEstoque saidaEstoque) {
		this.saidaEstoque = saidaEstoque;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
