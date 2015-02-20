package org.sales.medsales.web.action.movimento.estoque;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaldoProdutoVO;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;
import org.sales.medsales.negocio.movimentacao.estoque.ProdutoFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ProdutoAction extends CrudActionBase<Produto, Long, ProdutoFacade>{

	@Inject
	private Conversation conversation;
	
	@Inject
	private EstoqueFacade estoqueFacade;
	
	/** chave única para consulta */
	private String chaveConsulta;

	/** habilita a subconsulta para carregar estoque */
	private boolean exibirEstoque = true;
	
	/** para cadastro de um preço de produto */
	private PrecoProduto novoPreco;
	
	/** armazena os saldos dos produtos consultados */
	private Map<Long, SaldoProdutoVO> saldos = new HashMap<>();
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	@Override
	protected void configSearch(Filter<? extends Produto> filter) {
		super.configSearch(filter);
		/*
		 * Configura a consulta para usar um único campo do formulário como filtro
		 * associado a todos os atributos consultáveis do Produto
		 */
		filter.filterBy("nome", Operators.like(false), getChaveConsulta());
		filter.filterBy("codigoBarras", Operators.like(false), getChaveConsulta());
		filter.setRootContainerType(ContainerType.OR);
	}
	
	@Override
	protected void configLoad(Produto entidade, Filter<Produto> loadFilter) {
		super.configLoad(entidade, loadFilter);
		loadFilter.addFetch("precos");
	}

	@Override
	protected void doSave() {
		// verifica a necessidade de incluir um novo preço
		if (getNovoPreco() != null && getNovoPreco().getValor() != null) {
			getFacade().save(getEntity(), getNovoPreco());
		} else {
			super.doSave();
		}
	}
	
	@Override
	protected void postLoad() {
		super.postLoad();

		/*
		 * Ordenando a lista de precos para exibição
		 */
		getEntity().ordenarPrecos(false);
		
		/*
		 * No carregamento, caso o produto já tenha um preço cadastrado, utiliza seus dados
		 * como dados iniciais para o campo de valor do produto, dando a sugestão de manter
		 * o valor atual já cadastrado.
		 */
		
		setNovoPreco(new PrecoProduto());
		getNovoPreco().setProduto(getEntity());

		PrecoProduto precoAtual = getEntity().getPrecoAtual();
		if (precoAtual != null) {
			getNovoPreco().setValor(precoAtual.getValor());
		}	
	}
	
	@Override
	protected void postSearch(Filter<Produto> searchFilter) {
		super.postSearch(searchFilter);
		
		if (isExibirEstoque() && !getResultList().isEmpty()) {
			List<SaldoProdutoVO> saldoList = estoqueFacade.consultarEstoque(getResultList().toArray(new Produto[]{}));
			for (SaldoProdutoVO saldo : saldoList) {
				saldos.put(saldo.getIdProduto(), saldo);
			}
		}
	}
	
	@Override
	protected void initExample() {
		super.initExample();
		setChaveConsulta(null);
	}
	
	@Override
	protected void initEntity() {
		super.initEntity();
		setNovoPreco(new PrecoProduto());
	}
	
	public String getChaveConsulta() {
		return chaveConsulta;
	}

	public void setChaveConsulta(String chaveConsulta) {
		this.chaveConsulta = chaveConsulta;
	}

	public PrecoProduto getNovoPreco() {
		return novoPreco;
	}

	public void setNovoPreco(PrecoProduto novoPreco) {
		this.novoPreco = novoPreco;
	}

	public boolean isExibirEstoque() {
		return exibirEstoque;
	}

	public void setExibirEstoque(boolean exibirEstoque) {
		this.exibirEstoque = exibirEstoque;
	}

	/** @return O saldo do produto consultado */
	public SaldoProdutoVO getSaldo(Produto produto) {
		return saldos.get(produto.getId());
	}
}
