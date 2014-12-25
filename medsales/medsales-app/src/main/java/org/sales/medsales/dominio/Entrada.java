package org.sales.medsales.dominio;

import javax.persistence.ManyToOne;

/**
 * Caracteriza uma movimentação de Entrada, que acrescenta itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
public class Entrada extends MovimentacaoEstoque {

	/**
	 * Parceiro de onde os itens foram adquiridos.
	 */
	@ManyToOne
	private Parceiro parceiro;

	public Parceiro getParceiro() {
		return parceiro;
	}

	public void setParceiro(Parceiro parceiro) {
		this.parceiro = parceiro;
	}
	
}
