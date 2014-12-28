package org.sales.medsales.persistencia.repository;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Query;

import org.sales.medsales.dominio.MovimentacaoEstoque;
import org.sales.medsales.dominio.Produto;


public class EstoqueRepository extends CrudRepositoryBase<MovimentacaoEstoque, Long> {

	/**
	 * @return true se todos os produtos informados tiverem pelo menos um
	 * preço cadastrado.
	 */
	public boolean temPrecosCadastrados(Produto...produtos) {
		
		// montando uma lista com os IDs diferentes dos produtos
		Set<Long> idProdutos = new HashSet<>();
		for (Produto produto : produtos) {
			idProdutos.add(produto.getId());
		}
		
		// consultando os ids dos produtos com preços cadastrados
		Query query = getEm().createQuery(
				"   select count(distinct pp.produto.id) "
				+ " from PrecoProduto pp "
				+ " where pp.produto.id in (:idProdutos)");
		query.setParameter("idProdutos", idProdutos);
		
		Long numProdutosComPreco = (Long) query.getSingleResult();
		return numProdutosComPreco == idProdutos.size();
	}
}
