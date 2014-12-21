package com.sales.medsales.dominio;

import java.util.List;

/**
 * Representa um pedido de medicamentos.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class Pedido extends EntityBase<Long>{

	/**
	 * Todos os itens que compõem este pedido
	 */
	private List<Item> itens;
	
	/**
	 * Desconto aplicado a todos os items do pedido.
	 */
	private Double desconto;

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	
}
