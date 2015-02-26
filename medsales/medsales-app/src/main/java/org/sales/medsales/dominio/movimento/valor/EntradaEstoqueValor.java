package org.sales.medsales.dominio.movimento.valor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;

/**
 * Representa uma entrada de valor a partir de uma entrada no estoque.
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class EntradaEstoqueValor extends EntradaValor {

	/**
	 * Movimentação no estoque associada a esta movimentação de valor. 
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	private EntradaEstoque entradaEstoque;
	
	@Override
	public String getLabel() {
		return "Entrada em Estoque";
	}

	public EntradaEstoque getEntradaEstoque() {
		return entradaEstoque;
	}

	public void setEntradaEstoque(EntradaEstoque entradaEstoque) {
		this.entradaEstoque = entradaEstoque;
	}
}
