package org.sales.medsales.dominio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Caracteriza uma movimentação de Saída, que remove itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Saida extends MovimentacaoEstoque {

	/**
	 * Cliente que solicitou a saída (compra).
	 */
	@ManyToOne
	private Cliente cliente;
	
	/**
	 * Porcentagem de desconto aplicado sobre uma movimentação de saída.
	 */
	@Column(precision=4, scale=2)
	private Double desconto;

	@Override
	protected void defineOperacao() {
		setOperacao(Operacao.SAIDA);
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}
	
}
