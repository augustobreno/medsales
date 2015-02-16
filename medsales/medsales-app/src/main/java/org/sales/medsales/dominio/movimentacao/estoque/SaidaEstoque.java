package org.sales.medsales.dominio.movimentacao.estoque;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.sales.medsales.dominio.movimentacao.Operacao;

/**
 * Caracteriza uma movimentação de Saída, que remove itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class SaidaEstoque extends MovimentacaoEstoque {

	/**
	 * Porcentagem de desconto aplicado sobre uma movimentação de saída.
	 */
	@Column(precision=4, scale=2)
	private Double desconto;

	/**
	 * Numero sequencial para identificação de um pedido por cliente.
	 */
	private Integer numeroPedido;
	
	@Override
	protected void defineOperacao() {
		setOperacao(Operacao.SAIDA);
	}
	
	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	@Override
	public String getLabel() {
		return "Saída";
	}

	public Integer getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(Integer numeroPedido) {
		this.numeroPedido = numeroPedido;
	}
	
}
