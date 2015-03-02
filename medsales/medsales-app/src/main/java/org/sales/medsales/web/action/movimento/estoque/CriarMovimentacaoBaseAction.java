package org.sales.medsales.web.action.movimento.estoque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.primefaces.context.RequestContext;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.PrecoProduto;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.Status;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;
import org.sales.medsales.negocio.movimentacao.estoque.ProdutoFacade;

/**
 * Classe base que mantém as principais funcionalidades dos fluxos de cadastro e
 * manutenção de movimentações do tipo Entrada e Saída
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
public abstract class CriarMovimentacaoBaseAction<MOV extends MovimentoEstoque> extends ActionBase {

	@Inject
	private Conversation conversation;

	@Inject
	private ParceiroFacade parceiroFacade;

	@Inject
	private ProdutoFacade produtoFacade;

	@Inject
	private EstoqueFacade estoqueFacade;

	/**
	 * LoadId: GET parameter para carregamento dos dados de uma movimentação via
	 * URL.
	 */
	private Long lid;

	/**
	 * Entidade para manutenção dos dados no formulário da tela.
	 */
	private MOV movimentacao;

	/** Para preenchimento do formulário da lista de itens da movimentação */
	private Item item;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (conversation.isTransient()) {
			conversation.begin();
		}

		initMovimentacao();
		initItem();
	}

	/**
	 * Inicializa o item para o formulário de adição de produto.
	 */
	private void initItem() {
		item = new Item();
		item.setQuantidade(1); // valor inicial
	}

	/**
	 * Inicializa o objeto concreto que representará a movimentação
	 */
	protected abstract void initMovimentacao();

	/**
	 * Consulta por parceiros segundo a chave informada.
	 * 
	 * @param chave
	 *            Chave para consulta de parceiros.
	 * @return
	 */
	public List<Parceiro> searchParceiros(String chave) {
		Filter<Parceiro> filter = new QBEFilter<Parceiro>(new Parceiro());
		filter.getExample().setNome(chave);
		return parceiroFacade.findAllBy(filter);
	}

	/**
	 * Consulta por produtos segundo a chave informada.
	 * 
	 * @param chave
	 *            Chave para consulta de produtos, pode ser o nome do produto ou
	 *            o código de barras.
	 * @return Produtos encontrados.
	 */
	public List<PrecoProduto> searchProdutos(String chave) {

		/*
		 * Consultando apenas 15 produtos para otimização
		 */
		Filter<PrecoProduto> filter = new QBEFilter<PrecoProduto>(new PrecoProduto());
		filter.getExample().setProduto(new Produto());
		filter.getExample().getProduto().setNome(chave);
		filter.getExample().getProduto().setCodigoBarras(chave);
		filter.addFetch("produto");
		filter.setRootContainerType(ContainerType.OR);
		filter.paginate(0, 15);
		List<PrecoProduto> produtos = produtoFacade.findAllPrecoProdutoBy(filter);

		/*
		 * Para automatizar a interface, se a consulta resultar em apenas 1
		 * produto, e a chave de consulta for exatamente igual ao código de
		 * barras daquele, ele será automaticamente selecionado.
		 */
		if (produtos.size() == 1 && produtos.get(0).getProduto().getCodigoBarras().equalsIgnoreCase(chave)) {

			this.item.setPrecoProduto(produtos.get(0));
			if (this.item.getQuantidade() == null) {
				this.item.setQuantidade(1);
			}

			adicionarItem();

			// reseta a lista para prepara nova consulta.
			produtos = new ArrayList<PrecoProduto>();

			// atualizando a lista de itens programaticamente
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("listForm:itemList");

			/*
			 * limpando o campo "Produto" programaticamente porque há um bug
			 * quando ele é atualizado via ajax (o campo some!)
			 */
			context.execute("limparCampoProduto();");

		}

		return produtos;
	}

	/**
	 * Adiciona o item {@link #item} na lista de itens da movimentação.
	 */
	public void adicionarItem() {
		if (this.item != null && this.item.getPrecoProduto() != null && this.item.getQuantidade() > 0) {
			preAdicionarItem();
			Item itemAdded = doAdicionarItem();
			postAdicionarItem(itemAdded);
		} else {
			showErrorMessage("É necessário informar o Produto e a quantidade do Item.");
		}
	}

	/**
	 * Invocado após a adição de um item à movimentação
	 * 
	 * @param itemAdd
	 */
	protected void postAdicionarItem(Item itemAdd) {
		showInfoMessage("Produto \"{0}\" adicionado, total: \"{1}\"", itemAdd.getPrecoProduto().getProduto().getNome(),
				itemAdd.getQuantidade());

		initItem();
	}

	/**
	 * Adiciona, de fato, o item à movimentação.
	 * 
	 * @return Item adicionado.
	 */
	protected Item doAdicionarItem() {
		Item itemToAdd = buscarItem();
		if (itemToAdd == null) {
			// este produto ainda não foi inserido, será criado pela
			// primeira vez
			PrecoProduto precoProduto = getItem().getPrecoProduto();

			if (precoProduto == null) {
				throw new BusinessException(null,
						"Produto com código de barras \"{0}\" não existe ou não possui preço cadastrado.", getItem()
								.getPrecoProduto().getProduto().getCodigoBarras());
			}

			itemToAdd = new Item();
			itemToAdd.setPrecoProduto(precoProduto);
			itemToAdd.setQuantidade(this.item.getQuantidade());
			itemToAdd.setMovimentoEstoque(getMovimentacao());

			getItens().add(0, itemToAdd);

		} else {
			itemToAdd.incrementarQuantidade(this.item.getQuantidade());
		}
		return itemToAdd;
	}

	/**
	 * Invocando andes de adicionar um Item à movimentação
	 */
	protected void preAdicionarItem() {
		// TODO Auto-generated method stub

	}

	/**
	 * Busca o item selecionado na lista de itens
	 */
	protected Item buscarItem() {

		/*
		 * A chave de busca real é o código de barras em
		 * itemPreco.item.produto.codigoBarras
		 */
		Item original = null;
		for (Item itemInCol : getItens()) {
			if (itemInCol.getPrecoProduto().getProduto().getCodigoBarras()
					.equals(this.item.getPrecoProduto().getProduto().getCodigoBarras())) {
				original = itemInCol;
				break;
			}
		}
		return original;
	}

	public List<Item> getItens() {
		return getMovimentacao().getItens();
	}

	/**
	 * Salva a movimentação com status CONCLUÍDO.
	 */
	public void concluir() {
		getMovimentacao().setStatus(Status.CONCLUIDO);
		salvarMovimentacao();
		showInfoMessage("A {0} foi cadastrada com sucesso.", getMovimentacao().getLabel());
	}

	/**
	 * Operação para conduzir a persistência dos dados da movimentação.
	 */
	protected void salvarMovimentacao() {
		// transferindo os itens cadastrados para a movimentação.
		salvar();
	}

	/**
	 * Deve persisitir de fato a movimentação.
	 */
	protected abstract void salvar();

	/**
	 * Salva a movimentação parcipalmente.
	 */
	public void salvarRacunho() {
		getMovimentacao().setStatus(Status.RASCUNHO);
		salvarMovimentacao();
		showInfoMessage("A {0} foi salva como RASCUNHO, e não constará na contabilidade do estoque.", getMovimentacao()
				.getLabel());
	}

	/**
	 * Carrega uma movimentação quando há a presença do parâmetro {@link #lid}
	 */
	public void loadFromId() {
		preLoadFromId();
		doLoadId();
		postLoadId();
	}

	/**
	 * Após o carregamento pelo parâmetro GET lid.
	 */
	protected void postLoadId() {
		// TODO Auto-generated method stub
	}

	/**
	 * Carrega, de fato, a movimentação através do parâmetro GET lid.
	 */
	@SuppressWarnings("unchecked")
	protected void doLoadId() {
		if (lid != null) {
			Filter<MovimentoEstoque> filter = new QBEFilter<>(MovimentoEstoque.class);
			filter.filterBy("id", Operators.equal(), lid);
			filter.addFetch("itens.precoProduto.produto", "parceiro");
			MovimentoEstoque movimentacao = estoqueFacade.findBy(filter);

			if (movimentacao == null) {
				throw new BusinessException(null, "Nenhuma movimentação foi encontrada com o código informado: {0}",
						lid);
			}

			setMovimentacao((MOV) movimentacao);
		}
	}

	/**
	 * Antes do carregamento pelo parâmetro GET lid.
	 */
	protected void preLoadFromId() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return A soma dos valores dos itens acumulados nesta movimentação.
	 */
	public BigDecimal getValorTotalItens() {

		BigDecimal soma = BigDecimal.ZERO;
		if (getMovimentacao().getItens() != null) {
			for (Item item : getMovimentacao().getItens()) {
				Integer quantidade = item.getQuantidade();
				BigDecimal valor = item.getPrecoProduto().getValor();
				soma = soma.add(valor.multiply(new BigDecimal(quantidade)));
			}
		}

		return soma;
	}

	/**
	 * Remove a a movimentação.
	 */
	public void remover() {
		estoqueFacade.remover(getMovimentacao());
		showInfoMessage("A {0} com código {1} foi excluída com sucesso.", getMovimentacao().getLabel(),
				getMovimentacao().getId());
	}

	public MOV getMovimentacao() {
		return movimentacao;
	}

	public Item getItem() {
		return item;
	}

	protected void setMovimentacao(MOV movimentacao) {
		this.movimentacao = movimentacao;
	}

	protected void setItem(Item item) {
		this.item = item;
	}

	protected Conversation getConversation() {
		return conversation;
	}

	protected ParceiroFacade getParceiroFacade() {
		return parceiroFacade;
	}

	protected ProdutoFacade getProdutoFacade() {
		return produtoFacade;
	}

	protected EstoqueFacade getEstoqueFacade() {
		return estoqueFacade;
	}

	public Long getLid() {
		return lid;
	}

	public void setLid(Long lid) {
		this.lid = lid;
	}

	public boolean isConcluido() {
		return getMovimentacao() != null && Status.CONCLUIDO.equals(getMovimentacao().getStatus());
	}
}
