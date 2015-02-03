package org.sales.medsales.persistencia.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.easy.qbeasy.util.EntityUtil;
import org.sales.medsales.api.persistencia.repository.CrudRepositoryBase;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.SaldoProdutoVO;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.Operacao;

public class EstoqueRepository extends CrudRepositoryBase<MovimentacaoEstoque, Long> {

	/**
	 * @return true se todos os produtos informados tiverem pelo menos um preço
	 *         cadastrado.
	 */
	public boolean temPrecosCadastrados(Produto... produtos) {

		// montando uma lista com os IDs diferentes dos produtos
		Set<Long> idProdutos = new HashSet<>();
		for (Produto produto : produtos) {
			idProdutos.add(produto.getId());
		}

		// consultando os ids dos produtos com preços cadastrados
		Query query = getEm().createQuery(
				"   select count(distinct pp.produto.id) " + " from PrecoProduto pp "
						+ " where pp.produto.id in (:idProdutos)");
		query.setParameter("idProdutos", idProdutos);

		Long numProdutosComPreco = (Long) query.getSingleResult();
		return numProdutosComPreco == idProdutos.size();
	}

	/**
	 * Consulta o saldo em estoque de alguns produtos.
	 * 
	 * @param produtos
	 *            IDs dos produtos para consulta do saldo.
	 * @return Saldo existente em estoque.
	 */
	public List<SaldoProdutoVO> consultarSaldo(Produto...produtos) {

		// consultando os ids dos produtos com preços cadastrados
		String saldoClassName = SaldoProdutoVO.class.getName();
		Query query = getEm()
				.createQuery(
						" SELECT new "
								+ saldoClassName
								+ "(produto.id, SUM(case when mov.operacao = '"
								+ Operacao.ENTRADA.getId()
								+ "' then  item.quantidade else -item.quantidade end)) FROM Produto produto left join produto.itens item left join item.movimentacaoEstoque mov"
								+ " WHERE produto.id in (:idProdutos) GROUP BY produto ");
		query.setParameter("idProdutos", EntityUtil.getIds(produtos));

		@SuppressWarnings("unchecked")
		List<SaldoProdutoVO> saldos = query.getResultList();
		return saldos;

	}

	/**
	 * Consulta o maior número de pedido para um cliente.
	 * @param parceiro Cliente para consulta.
	 * @return O maior número de pedido cadastrado para este cliente.
	 */
	public int buscarMaiorNumeroPedido(Parceiro parceiro) {
		Query query = getEm().createQuery("SELECT max(saida.numeroPedido) FROM Saida saida where saida.parceiro = :parceiro");
		query.setParameter("parceiro", parceiro);
		Integer maxNumero = (Integer) query.getSingleResult();
		return maxNumero != null ? maxNumero : 0;
	}

}
