package org.sales.medsales.dominio;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

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
	private MovimentacaoEstoque tipoMovimentacao;
	
	/**
	 * Quantidade de produtos pedidos.
	 */
	private Long quantidade;

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public MovimentacaoEstoque getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(MovimentacaoEstoque tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
}
