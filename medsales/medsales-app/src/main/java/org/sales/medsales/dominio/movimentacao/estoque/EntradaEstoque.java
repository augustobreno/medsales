package org.sales.medsales.dominio.movimentacao.estoque;

import javax.persistence.Entity;

import org.sales.medsales.dominio.movimentacao.Operacao;

/**
 * Caracteriza uma movimentação de Entrada, que acrescenta itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class EntradaEstoque extends MovimentoEstoque {

	@Override
	protected void defineOperacao() {
		setOperacao(Operacao.ENTRADA);
	}

	@Override
	public String getLabel() {
		return "Entrada";
	}
}
