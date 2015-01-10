package org.sales.medsales.dominio;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.sales.medsales.api.dominio.EntityBase;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;

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
	private Produto produto;

	/**
	 * Determina o comportamento deste item de estoque (entrada/saída)
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private MovimentacaoEstoque movimentacaoEstoque;
	
	/**
	 * Quantidade de produtos pedidos.
	 */
	private Integer quantidade;

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public MovimentacaoEstoque getMovimentacaoEstoque() {
		return movimentacaoEstoque;
	}

	public void setMovimentacaoEstoque(MovimentacaoEstoque tipoMovimentacao) {
		this.movimentacaoEstoque = tipoMovimentacao;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	/**
	 * Incrementa a quantidade deste item.
	 */
	public void incrementarQuantidade(int incremento) {
		this.quantidade += incremento;
	}
}
