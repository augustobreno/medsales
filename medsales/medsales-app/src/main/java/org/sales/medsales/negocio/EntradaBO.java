package org.sales.medsales.negocio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.sales.medsales.api.exceptions.NullParameterException;
import org.sales.medsales.dominio.Entrada;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.exceptions.MovimentacaoSemItensException;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;
import org.sales.medsales.persistencia.repository.EstoqueRepository;

/**
 * Business Object com regras de negócio para o cadastro de uma entrada.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
public class EntradaBO implements Serializable {

	@Inject
	private EstoqueRepository estoqueRepository;
	
	/**
	 * Cadastra uma entrada de produtos.
	 */
	public void cadastrar(Entrada entrada) {
		validarDependenciasObrigatorias(entrada);
		validarPrecosDeProdutos(entrada);
		estoqueRepository.insert(entrada);
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
		
		boolean temPrecosCagastados = estoqueRepository.temPrecosCadastrados(produtos.toArray(new Produto[]{}));
		if (!temPrecosCagastados) {
			throw new ProdutoSemPrecoException(null, "Todos os produtos da Entrada devem ter preço cadastrado.");
		}	
	}

	protected void validarDependenciasObrigatorias(Entrada entrada) {
		if (entrada == null) {
			throw new NullParameterException("É necessário informar uma entrada.");
		}
		if (entrada.getOperacao() == null) {
			throw new NullParameterException("É necessário informar o tipo da operação da entrada.");
		}
		if (entrada.getItens() == null || entrada.getItens().isEmpty()) {
			throw new MovimentacaoSemItensException(null, "É necessário incluir Itens na movimentação.");
		}
	}	

}
