package org.sales.medsales.negocio.movimentacao;

import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.dominio.movimentacao.Status;


/**
 * Business Object com regras de negócio para o cadastro de uma saída.
 * @author Augusto
 */
@SuppressWarnings("serial")
public class SaidaBO extends MovimentacaoBO<Saida>{

	@Override
	public void salvar(Saida saida) {
		gerarNumeroPedido(saida);
		super.salvar(saida);
	}

	/**
	 * Gera o número sequencial de um pedido, considerando uma sequência independente
	 * para cada cliente. 
	 */
	public void gerarNumeroPedido(Saida saida) {
		if (Status.CONCLUIDO.equals(saida.getStatus())
				&& saida.getParceiro() != null
				&& saida.getNumeroPedido() == null) {
			
			int maiorNumPedido = getEstoqueRepository().buscarMaiorNumeroPedido(saida.getParceiro());
			saida.setNumeroPedido(++maiorNumPedido);
		}
	}
	
}
