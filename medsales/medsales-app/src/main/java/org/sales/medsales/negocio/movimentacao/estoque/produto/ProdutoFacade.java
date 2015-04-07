package org.sales.medsales.negocio.movimentacao.estoque.produto;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.negocio.CrudFacadeBase;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.exceptions.ProdutoCodBarrasJaExisteException;
import org.sales.medsales.exceptions.RemoverProdutoComMovimentacaoException;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;
import org.sales.medsales.persistencia.repository.PrecoProdutoRepository;
import org.sales.medsales.persistencia.repository.ProdutoRepository;

@SuppressWarnings("serial")
@Stateless
public class ProdutoFacade extends CrudFacadeBase<ProdutoRepository, Produto, Long> {

	@Inject
	private Instance<PrecoProdutoRepository> precoProdutoRepositoryInstance;

	@Inject
	private Instance<EstoqueFacade> estoqueFacadeInstance;
	
	@Inject
	private Instance<AddPrecoProdutoBO> addPrecoProdutoBO;

	/**
	 * @param codBarras Código de barras de um produto.
	 * @return Preco do produto.
	 */
	public PrecoProduto buscarPrecoProduto(String codBarras) {
		QBEFilter<PrecoProduto> filter = new QBEFilter<PrecoProduto>(PrecoProduto.class);
		filter.filterBy("produto.codigoBarras", Operators.equal(), codBarras);
		filter.addFetch("produto");
		filter.sortDescBy("validoEm", "id"); // determina o valor mais recente
		filter.paginate(0, 1);
		PrecoProduto preco = getPrecoProdutoRepository().findBy(filter);
		return preco;
	}

	@Override
	public Produto save(Produto entity) {
		// salva o produto com o preço mais recente informado.
		return save(entity, entity.getPrecoAtual());
	}
	
	@Override
	protected void validateSave(Produto entity) {
		if (!checkUniqueConstraint(entity, "codigoBarras")) {
			throw new ProdutoCodBarrasJaExisteException(null, "Este código de barras já está cadastrado.");
		}
		super.validateSave(entity);
	}
	
	
	public Produto save(Produto entity, PrecoProduto precoProduto) {
		return getAddPrecoProdutoBO().addPreco(entity, precoProduto);
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
		List<PrecoProduto> precos = getPrecoProdutoRepository().findAllBy(filter);
		for (PrecoProduto precoProduto : precos) {
			getPrecoProdutoRepository().remove(precoProduto);
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
		QBEFilter<MovimentoEstoque> filter = new QBEFilter<>(MovimentoEstoque.class);
		filter.filterBy("itens.precoProduto.produto", Operators.equal(), entity);

		long numMovimentacoes = getEstoqueFacade().count(filter);
		if (numMovimentacoes > 0) {
			throw new RemoverProdutoComMovimentacaoException(null,
					"Não é possível remover este Produto pois já foi utilizado em uma Movimentação.");
		}
	}
	
    public List<PrecoProduto> findAllPrecoProdutoBy(Filter<PrecoProduto> filter) {
        return getPrecoProdutoRepository().findAllBy(filter);
    }

    protected PrecoProdutoRepository getPrecoProdutoRepository() {
    	return precoProdutoRepositoryInstance.get();
    }
    
    protected EstoqueFacade getEstoqueFacade() {
    	return estoqueFacadeInstance.get();
    }
    
    protected AddPrecoProdutoBO getAddPrecoProdutoBO() {
    	return addPrecoProdutoBO.get();
    }
    
}
