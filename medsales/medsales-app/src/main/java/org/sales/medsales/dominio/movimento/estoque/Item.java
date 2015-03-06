package org.sales.medsales.dominio.movimento.estoque;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.sales.medsales.api.dominio.EntityBase;

/**
 * Item para composição de um pedido de aquisição ou venda. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Item extends EntityBase<Long> {

	/**
	 * Produto associado a este pedido.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private PrecoProduto precoProduto;

	/**
	 * Determina o comportamento deste item de estoque (entrada/saída)
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private MovimentoEstoque movimentoEstoque;
	
	/**
	 * Quantidade de produtos pedidos.
	 */
	private Integer quantidade;

	public BigDecimal calcularPrecoTotal() {
		return getPrecoProduto().getValor().multiply(new BigDecimal(getQuantidade()));
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	public void setMovimentoEstoque(MovimentoEstoque tipoMovimentacao) {
		this.movimentoEstoque = tipoMovimentacao;
	}

	public PrecoProduto getPrecoProduto() {
		return precoProduto;
	}

	public void setPrecoProduto(PrecoProduto produto) {
		this.precoProduto = produto;
	}
	
	/**
	 * Incrementa a quantidade deste item.
	 */
	public void incrementarQuantidade(int incremento) {
		this.quantidade += incremento;
	}
}
