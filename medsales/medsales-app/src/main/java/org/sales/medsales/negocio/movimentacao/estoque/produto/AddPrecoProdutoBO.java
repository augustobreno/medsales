package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.util.Date;

import javax.inject.Inject;

import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.exceptions.ProdutoSemPrecoException;
import org.sales.medsales.persistencia.repository.PrecoProdutoRepository;
import org.sales.medsales.persistencia.repository.ProdutoRepository;

/**
 * Contém a lógica para cadastrar um preco para um produto 
 * @author Augusto
 *
 */
public class AddPrecoProdutoBO {

	@Inject
	private ProdutoRepository produtoRepository;
	
	@Inject
	private PrecoProdutoRepository precoProdutoRepository;

	/**
	 * Adiciona um preço ao produto e salva a informação.
	 */
	public Produto addPreco(Produto entity, PrecoProduto precoProduto) {
		if (precoProduto == null || precoProduto.getValor() == null) {
			throw new ProdutoSemPrecoException(null, "Preco do Produto deve ser informado.");
		}

		Produto saved = entity.getId() == null 
				? produtoRepository.insert(entity)
				: produtoRepository.update(entity);

		// processa o preço do produto em seguida.
		Produto original = produtoRepository.findBy(entity.getId(), "precos");
		PrecoProduto precoAtual = original.getPrecoAtual();

		if (precoAtual == null || !precoAtual.getValor().equals(precoProduto.getValor())) {
			precoProduto.setProduto(saved);
			if (precoProduto.getValidoEm() == null) {
				precoProduto.setValidoEm(new Date());
			}
			precoProdutoRepository.insert(precoProduto);
		}

		return saved;
	}
	
	/**
	 * Atualiza um preço do produto e salva a informação.
	 */
	public Produto replacePreco(Produto entity, PrecoProduto precoProduto) {
		if (precoProduto == null || precoProduto.getValor() == null) {
			throw new ProdutoSemPrecoException(null, "Preco do Produto deve ser informado.");
		}

		Produto original;
		if (entity.getId() == null) {
			original = produtoRepository.insert(entity);
		} else {
			original = produtoRepository.findBy(entity.getId(), "precos");
			original.setNome(entity.getNome());
			produtoRepository.update(original);
		}
		
		// processa o preço do produto em seguida.
		PrecoProduto precoAtual = original.getPrecoAtual();

		
		if (precoAtual == null || !precoAtual.getValor().equals(precoProduto.getValor())) {
			precoProduto.setProduto(original);
			if (precoProduto.getValidoEm() == null) {
				precoProduto.setValidoEm(new Date());
			}
			precoProdutoRepository.insert(precoProduto);
		}

		return original;
	}

}
