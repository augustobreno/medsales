package org.sales.medsales.negocio.movimentacao.estoque;

import java.util.ArrayList;
import java.util.List;

import org.sales.medsales.dominio.movimentacao.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimentacao.estoque.Item;
import org.sales.medsales.dominio.movimentacao.estoque.Produto;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;

/**
 * Business Object com regras de negócio para o cadastro de uma entrada.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class EntradaEstoqueBO extends MovimentacaoEstoqueBO<EntradaEstoque> {

	@Override
	protected void validarCadastrar(EntradaEstoque movimentacao) {
		super.validarCadastrar(movimentacao);
		validarPrecosDeProdutos(movimentacao);
	}

	/**
	 * Uma entrada só pode ser cadastrada se todos os produtos associados
	 * estiverem com um preço cadastrado. 
	 */
	protected void validarPrecosDeProdutos(EntradaEstoque entradaEstoque) {
		
		List<Produto> produtos = new ArrayList<>();
		for (Item item : entradaEstoque.getItens()) {
			produtos.add(item.getProduto());
		}
		
		boolean temPrecosCagastados = getEstoqueRepository().temPrecosCadastrados(produtos.toArray(new Produto[]{}));
		if (!temPrecosCagastados) {
			throw new ProdutoSemPrecoException(null, "Todos os produtos da Entrada devem ter preço cadastrado.");
		}	
	}

}
