package org.sales.medsales.web.action.movimentacao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.primefaces.context.RequestContext;
import org.sales.medsales.api.exceptions.BusinessException;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.Item;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.dominio.PrecoProduto;
import org.sales.medsales.dominio.Produto;
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.Status;
import org.sales.medsales.negocio.EstoqueFacade;
import org.sales.medsales.negocio.ParceiroFacade;
import org.sales.medsales.negocio.ProdutoFacade;

/**
 * Para cadastro de uma Entrada de Produtos no estoque
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaAction extends ActionBase {

	@Inject
	private Conversation conversation;

	@Inject
	private ParceiroFacade parceiroFacade;

	@Inject
	private ProdutoFacade produtoFacade;

	@Inject
	private EstoqueFacade estoqueFacade;

	/** Para cadastro dos dados da entrada */
	private Entrada entrada;

	/** Para preenchimento da lista de itens da entrada */
	private Item item;

	/**
	 * Trabalhando com uma lista de itens "externa" à entrada para poder usar um
	 * tipo específico que permite maior manipulação da posição dos objetos
	 */
	private LinkedList<ItemPreco> itens;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (conversation.isTransient()) {
			conversation.begin();
		}

		entrada = new Entrada();

		itens = new LinkedList<ItemPreco>();

		initItem();
	}

	private void initItem() {
		item = new Item();
		item.setQuantidade(1); // valor inicial
	}

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
	public List<Produto> searchProdutos(String chave) {
		
		/*
		 * Consultando apenas 10 produtos para otimização
		 */
		Filter<Produto> filter = new QBEFilter<Produto>(new Produto());
		filter.getExample().setNome(chave);
		filter.getExample().setCodigoBarras(chave);
		filter.setRootContainerType(ContainerType.OR);
		List<Produto> produtos = produtoFacade.findAllBy(filter);

		/*
		 * Para automatizar a interface, se a consulta resultar em apenas 1
		 * produto, e a chave de consulta for exatamente igual ao código de
		 * barras daquele, ele será automaticamente selecionado.
		 */
		if (produtos.size() == 1 && produtos.get(0).getCodigoBarras().equalsIgnoreCase(chave)) {

			this.item.setProduto(produtos.get(0));
			if (this.item.getQuantidade() == null) {
				this.item.setQuantidade(1);
			}

			adicionarItem();

			// reseta a lista para prepara nova consulta.
			produtos = new ArrayList<Produto>();

			// atualizando a lista de itens programaticamente
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("itemForm:itemList");

			/*
			 * limpando o campo "Produto" programaticamente porque há um bug
			 * quando ele é atualizado via ajax (o campo some!)
			 */
			context.execute("limparCampoProduto();");

		}

		return produtos;
	}

	/**
	 * Adiciona o item {@link #item} na lista de itens da entrada.
	 */
	public void adicionarItem() {
		if (this.item != null && this.item.getProduto() != null && this.item.getQuantidade() > 0) {
			ItemPreco itemPreco = buscarItem();
			if (itemPreco == null) {
				// este produto ainda não foi inserido, será criado pela
				// primeira vez
				PrecoProduto precoProduto = produtoFacade.buscarPrecoProduto(getItem().getProduto().getCodigoBarras());

				if (precoProduto == null) {
					throw new BusinessException(null,
							"Produto com código de barras \"{0}\" não existe ou não possui preço cadastrado.",
							getItem().getProduto().getCodigoBarras());
				}

				Item novoItem = new Item();
				novoItem.setProduto(precoProduto.getProduto());
				novoItem.setQuantidade(this.item.getQuantidade());
				novoItem.setMovimentacaoEstoque(entrada);

				itemPreco = new ItemPreco(novoItem, precoProduto);
				getItens().addFirst(itemPreco);

			} else {
				itemPreco.getItem().incrementarQuantidade(this.item.getQuantidade());
			}

			showInfoMessage("Produto \"{0}\" adicionado, total: \"{1}\"", itemPreco.getItem().getProduto().getNome(),
					itemPreco.getItem().getQuantidade());

			initItem();
		} else {
			showErrorMessage("É necessário informar o Produto e a quantidade do Item.");
		}
	}

	/**
	 * Busca o item selecionado na lista de itens
	 */
	private ItemPreco buscarItem() {

		/*
		 * A chave de busca real é o código de barras em
		 * itemPreco.item.produto.codigoBarras
		 */
		ItemPreco itemPreco = new ItemPreco(this.item, null);
		int itemPrecoIndex = getItens().indexOf(itemPreco);
		return itemPrecoIndex >= 0 ? getItens().get(itemPrecoIndex) : null;
	}

	/**
	 * Salva a entrada com status CONCLUÍDO.
	 */
	public void concluir() {
		// trasnferindo os itens cadastrados para a entrada.
		List<Item> entradaItens = new ArrayList<Item>();
		for (ItemPreco itemPreco : this.itens) {
			entradaItens.add(itemPreco.getItem());
		}

		entrada.setItens(entradaItens);
		entrada.setStatus(Status.CONCLUIDO);
		estoqueFacade.cadastrarEntrada(entrada);
		
		showInfoMessage("A Entrada foi cadastrada com sucesso.");
	}

	/**
	 * Salva a entrada parcipalmente.
	 */
	public void salvarRacunho() {
		
	}
	
	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public LinkedList<ItemPreco> getItens() {
		return itens;
	}

	public void setItens(LinkedList<ItemPreco> itens) {
		this.itens = itens;
	}

	public void preRender() {
	}

	/**
	 * Decorator para exibição conjunta dos dados do Item e o preço cadastro.
	 * 
	 * @author Augusto
	 */
	public class ItemPreco {

		private Item item;
		private PrecoProduto preco;

		public ItemPreco() {
			super();
		}

		public ItemPreco(Item item, PrecoProduto preco) {
			super();
			this.item = item;
			this.preco = preco;
		}

		public PrecoProduto getPreco() {
			return preco;
		}

		public void setPreco(PrecoProduto preco) {
			this.preco = preco;
		}

		public Item getItem() {
			return item;
		}

		public void setItem(Item item) {
			this.item = item;
		}

		/**
		 * A chave de comparação real é o código de barras em
		 * itemPreco.item.produto.codigoBarras
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj instanceof ItemPreco) {
				ItemPreco outroItem = (ItemPreco) obj;
				return this.getItem().getProduto().getCodigoBarras()
						.equals(outroItem.getItem().getProduto().getCodigoBarras());
			}
			return false;
		}
	}
}
