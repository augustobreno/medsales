package org.sales.medsales.negocio.movimentacao.estoque;

import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Status;


/**
 * Business Object com regras de negócio para o cadastro de uma saída.
 * @author Augusto
 */
@SuppressWarnings("serial")
public class SaidaEstoqueBO extends MovimentacaoEstoqueBO<SaidaEstoque>{

	@Override
	public void persistir(SaidaEstoque saidaEstoque) {
//		gerarNumeroPedido(saidaEstoque);
		super.persistir(saidaEstoque);
	}

	/**
	 * Gera o número sequencial de um pedido, considerando uma sequência independente
	 * para cada cliente. 
	 */
	public void gerarNumeroPedido(SaidaEstoque saidaEstoque) {
		if (Status.CONCLUIDO.equals(saidaEstoque.getStatus())
				&& saidaEstoque.getParceiro() != null
				&& saidaEstoque.getNumeroPedido() == null) {
			
			int maiorNumPedido = getEstoqueRepository().buscarMaiorNumeroPedido(saidaEstoque.getParceiro());
			saidaEstoque.setNumeroPedido(String.valueOf(++maiorNumPedido));
		}
	}
	
}
