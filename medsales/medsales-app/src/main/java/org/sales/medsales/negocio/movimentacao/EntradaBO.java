package org.sales.medsales.negocio.movimentacao;

import java.util.ArrayList;
import java.util.List;

import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;

/**
 * Business Object com regras de negócio para o cadastro de uma entrada.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class EntradaBO extends MovimentacaoBO {

	@Override
	protected void validarCadastrar(MovimentacaoEstoque movimentacao) {
		super.validarCadastrar(movimentacao);
		validarPrecosDeProdutos((Entrada) movimentacao);
	}

	/**
	 * Uma entrada só pode ser cadastrada se todos os produtos associados
	 * estiverem com um preço cadastrado. 
	 */
	protected void validarPrecosDeProdutos(Entrada entrada) {
		
		List<Produto> produtos = new ArrayList<>();
		for (Item item : entrada.getItens()) {
			produtos.add(item.getProduto());
		}
		
		boolean temPrecosCagastados = getEstoqueRepository().temPrecosCadastrados(produtos.toArray(new Produto[]{}));
		if (!temPrecosCagastados) {
			throw new ProdutoSemPrecoException(null, "Todos os produtos da Entrada devem ter preço cadastrado.");
		}	
	}

}
