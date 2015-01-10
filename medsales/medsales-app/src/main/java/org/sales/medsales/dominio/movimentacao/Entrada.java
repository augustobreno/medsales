package org.sales.medsales.dominio.movimentacao;

import javax.persistence.Entity;

/**
 * Caracteriza uma movimentação de Entrada, que acrescenta itens ao estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Entity
public class Entrada extends MovimentacaoEstoque {

	@Override
	protected void defineOperacao() {
		setOperacao(Operacao.ENTRADA);
	}
	
}
