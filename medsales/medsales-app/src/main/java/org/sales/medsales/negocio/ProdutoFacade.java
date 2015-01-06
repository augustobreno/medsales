package org.sales.medsales.negocio;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.MovimentacaoEstoque;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.exceptions.RemoverProdutoComMovimentacaoException;
import org.sales.medsales.persistencia.repository.PrecoProdutoRepository;
import org.sales.medsales.persistencia.repository.ProdutoRepository;

@SuppressWarnings("serial")
@Stateless
public class ProdutoFacade extends CrudFacadeBase<ProdutoRepository, Produto, Long> {

	@Inject
	private PrecoProdutoRepository precoProdutoRepository;

	@Inject
	private EstoqueFacade estoqueFacade;

	/**
	 * @param codBarras Código de barras de um produto.
	 * @return Preco do produto.
	 */
	public PrecoProduto buscarPrecoProduto(String codBarras) {
		//TODO não está considerando o preço mais recente
		QBEFilter<PrecoProduto> filter = new QBEFilter<PrecoProduto>(PrecoProduto.class);
		filter.filterBy("produto.codigoBarras", Operators.equal(), codBarras);
		filter.addFetch("produto");
		PrecoProduto preco = precoProdutoRepository.findBy(filter);
		return preco;
	}

	// TODO validar código de barras duplicado
	public Produto save(Produto entity, PrecoProduto precoProduto) {
		if (precoProduto == null || precoProduto.getValor() == null) {
			throw new IllegalArgumentException("Preco do Produto deve ser informado.");
		}

		Produto saved = super.save(entity);

		// processa o preço do produto em seguida.
		Produto original = getRepository().findBy(entity.getId(), "precos");
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

	@Override
	public void remove(Produto entity) {
		validateRemove(entity);
		removerPrecosAssociados(entity);
		getRepository().remove(entity);
	}

	private void removerPrecosAssociados(Produto entity) {
		// FIXME implementando na mão porque o cascade não funcionou. Revisar!
		QBEFilter<PrecoProduto> filter = new QBEFilter<PrecoProduto>(PrecoProduto.class);
		filter.setExample(new PrecoProduto());
		filter.getExample().setProduto(entity);
		List<PrecoProduto> precos = precoProdutoRepository.findAllBy(filter);
		for (PrecoProduto precoProduto : precos) {
			precoProdutoRepository.remove(precoProduto);
		}
	}

	@Override
	protected void validateRemove(Produto entity) {
		super.validateRemove(entity);
		validarDependenciaMovimentacao(entity);
	}

	private void validarDependenciaMovimentacao(Produto entity) {
		// Não é possível remover um produto se este já foi cadastrado em uma
		// movimentacao
		QBEFilter<MovimentacaoEstoque> filter = new QBEFilter<>(MovimentacaoEstoque.class);
		filter.filterBy("itens.produto", Operators.equal(), entity);

		long numMovimentacoes = estoqueFacade.count(filter);
		if (numMovimentacoes > 0) {
			throw new RemoverProdutoComMovimentacaoException(null,
					"Não é possível remover este Produto pois já foi utilizado em uma Movimentação.");
		}
	}
}
