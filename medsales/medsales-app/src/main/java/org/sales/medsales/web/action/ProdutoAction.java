package org.sales.medsales.web.action;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.negocio.ProdutoFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ProdutoAction extends CrudActionBase<Produto, Long, ProdutoFacade>{

	@Inject
	private Conversation conversation;
	
	/** chave única para consulta */
	private String chaveConsulta;
	
	private PrecoProduto novoPreco;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		conversation.begin();
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

	
}
