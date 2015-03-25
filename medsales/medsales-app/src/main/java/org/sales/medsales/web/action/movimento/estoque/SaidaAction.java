package org.sales.medsales.web.action.movimento.estoque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.Produto;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaldoProdutoVO;
import org.sales.medsales.util.CalculosUtil;

/**
 * Mantém o fluxo de cadastro e manutenção de saída (venda) de produtos.
 * 
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class SaidaAction extends CriarMovimentacaoBaseAction<SaidaEstoque> {

	/** armazena os saldos dos produtos consultados */
	private Map<Long, SaldoProdutoVO> saldos = new HashMap<>();
	
	@Override
	protected void initMovimentacao() {
		SaidaEstoque saidaEstoque = new SaidaEstoque();
		saidaEstoque.setItens(new LinkedList<Item>());
		saidaEstoque.setDataMovimento(new Date());
		setMovimentacao(saidaEstoque);
	}
	
	@Override
	protected void salvar() {
		getEstoqueFacade().salvar(getMovimentacao());
	}
	
	@Override
	protected void postAdicionarItem(Item itemAdd) {
		super.postAdicionarItem(itemAdd);
		consultarSaldo(itemAdd.getPrecoProduto().getProduto());
	}

	@Override
	protected void postLoadId() {
		super.postLoadId();
		consultarSaldo(getProdutos());
	}
	
	private Produto[] getProdutos() {
		List<Produto> produtos = new ArrayList<Produto>();
		
		for (Item item : getItens()) {
			produtos.add(item.getPrecoProduto().getProduto());
		}
		
		return produtos.toArray(new Produto[]{});
	}

	/**
	 * Consulta o saldo em estoque dos produtos.
	 */
	private void consultarSaldo(Produto...produtos) {
		// quando adicionar apenas um produto, verifica se o saldo já foi consultado
		if (produtos != null 
				&& produtos.length > 0	
				&& (produtos.length > 1 || !isSaldoConsultado(produtos[0]))) {
			
			List<SaldoProdutoVO> saldoList = getEstoqueFacade().consultarEstoque(getMovimentacao(), produtos);
			for (SaldoProdutoVO saldo : saldoList) {
				saldos.put(saldo.getIdProduto(), saldo);
			}
			
		}	
	}
	
	@Override
	protected void configLoadFromIdFilter(Filter<MovimentoEstoque> filter, Long idMovimento) {
		super.configLoadFromIdFilter(filter, idMovimento);
		filter.setEntityClass(SaidaEstoque.class);
		filter.addFetch("notasCompra");
	}
	
	/**
	 * Calculando o total considerando o desconto
	 */
	@Override
	public BigDecimal calcularPrecoTotal(Item item) {
		BigDecimal total = super.calcularPrecoTotal(item);
		return getMovimentacao().getDesconto() == null ? total : CalculosUtil.aplicarDesconto(total, getMovimentacao().getDesconto());
	}

	/**
	 * Operação invocada quando o usuário informa um valor para desconto e aplica sobre o movimento.
	 */
	public void aplicarDesconto() {
		// os valores totais são calculados diretamente na página.
		// resta salvar automaticamente, quando ativado
		salvarAutomaticamente();
	}
	
	private boolean isSaldoConsultado(Produto produto) {
		return saldos.containsKey(produto.getId());
	}

	/** @return O saldo do produto consultado */
	public SaldoProdutoVO getSaldo(Produto produto) {
		return saldos.get(produto.getId());
	}
	
//	public void createPDF() {
//	    FacesContext facesContext = FacesContext.getCurrentInstance();
//	    ExternalContext externalContext = facesContext.getExternalContext();
//	    String servername = externalContext.getRequestServerName();
//	    String port = String.valueOf(externalContext.getRequestServerPort());
//	    String appname = externalContext.getRequestContextPath();
//	    String protocol = externalContext.getRequestScheme();
//	    this.url = protocol + "://" + servername + ":" + port + appname + PDF_PAGE;
//	    try {
//	        ITextRenderer renderer = new ITextRenderer();
//	        renderer.setDocument(new URL(url).toString());
//	        renderer.layout();
//	        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
//	        response.reset();
//	        response.setContentType("application/pdf");
//	        response.setHeader("Content-Disposition", "inline; filename=\"" + PDF_FILE_NAME + "\"");
//	        OutputStream browserStream = response.getOutputStream();
//	        renderer.createPDF(browserStream);
//
//	    } catch (Exception ex) {
//	        Logger.getLogger(PdfBean.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	    facesContext.responseComplete();
//	}
}
