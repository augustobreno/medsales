package org.sales.medsales.negocio.movimentacao;

import java.util.List;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.SaldoProdutoVO;
import org.sales.medsales.exceptions.ExceptionCodes;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Realiza as operações relacionadas à consulta e verificação de saldo em
 * estoque de produtos.
 * 
 * @author Augusto
 *
 */
public class SaldoEstoqueBO {

	@Inject
	private EstoqueRepository estoqueRepository;

	/**
	 * Consulta o saldo em estoque de um determinado produto.
	 * 
	 * @param produto
	 *            Produto para consulta do saldo.
	 * @return Saldo existente em estoque.
	 */
	public SaldoProdutoVO consultar(Produto produto) {
		if (produto == null) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO.SALDO_PRODUTO_REQUIRED,
					"É necessário informar o Produto para consultar o saldo.");
		}
		
		List<SaldoProdutoVO> saldo = estoqueRepository.consultarSaldo(produto);
		return saldo != null && !saldo.isEmpty() ? saldo.get(0) : null;
	}
	
	/**
     * Consulta o saldo em estoque de alguns produtos.
     * @param produtos Produto para consulta do saldo.
     * @return Lista com saldos existentes em estoque.
     */
	public List<SaldoProdutoVO> consultar(Produto...produtos) {
		if (produtos == null || produtos.length == 0) {
			throw new NullParameterException(ExceptionCodes.MOVIMENTACAO.SALDO_PRODUTO_REQUIRED,
					"É necessário informar o Produto para consultar o saldo.");
		}
		
		return estoqueRepository.consultarSaldo(produtos);
	}

}
