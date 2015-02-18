package org.sales.medsales.persistencia.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.easy.qbeasy.util.EntityUtil;
import org.sales.medsales.api.persistencia.repository.CrudRepositoryBase;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimento.Operacao;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaldoProdutoVO;

public class EstoqueRepository extends CrudRepositoryBase<MovimentoEstoque, Long> {

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
		String precoProdutoType = PrecoProduto.class.getSimpleName();
		Query query = getEm().createQuery(
				"   select count(distinct pp.produto.id) from " + precoProdutoType + " pp "
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
		String produtoType = Produto.class.getSimpleName();
		Query query = getEm()
				.createQuery(
						" SELECT new "
								+ saldoClassName
								+ "(produto.id, SUM(case when mov.operacao = '"
								+ Operacao.ENTRADA.getId()
								+ "' then  item.quantidade else -item.quantidade end)) "
								+ "FROM " + produtoType + " produto left join produto.itens item left join item.movimentoEstoque mov"
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
		String saidaType = SaidaEstoque.class.getSimpleName();
		Query query = getEm().createQuery("SELECT max(saida.numeroPedido) FROM " + saidaType + " saida where saida.parceiro = :parceiro");
		query.setParameter("parceiro", parceiro);
		Integer maxNumero = (Integer) query.getSingleResult();
		return maxNumero != null ? maxNumero : 0;
	}

}
