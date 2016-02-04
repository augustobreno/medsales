package org.sales.medsales.web.action.movimento.estoque;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.dominio.Configuracao;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.negocio.ConfiguracaoFacade;
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

	@Inject
	private ConfiguracaoFacade configuracaoFacade;
	
	private String titulo;
	
	@Override
	public void loadFromId() {
		super.loadFromId();
		setTitulo();
	}
	
	@Override
	protected void initMovimentacao() {
		SaidaEstoque saidaEstoque = new SaidaEstoque();
		saidaEstoque.setItens(new LinkedList<Item>());
		saidaEstoque.setDataMovimento(new Date());
		
		Configuracao configuracao = configuracaoFacade.getConfiguracao();
		saidaEstoque.setIndiceComissao(configuracao.getIndiceComissao());
		saidaEstoque.setIndiceInvestimento(configuracao.getIndiceInvestimento());
		
		setMovimentacao(saidaEstoque);
	}
	
	@Override
	protected void salvar() {
		getEstoqueFacade().salvar(getMovimentacao());
		setTitulo();
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

	protected void setTitulo() {
		
		SaidaEstoque movimento = getMovimentacao() != null ? getMovimentacao() : new SaidaEstoque(); 
		
		String format = "Pedido: {0} em {1} - Nº {2}";
		String titulo = 
				MessageFormat.format(format, 
					movimento.getParceiro() != null ?  movimento.getParceiro().getNome() : "<Parceiro>",
					movimento.getDataMovimento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(movimento.getDataMovimento()) : "<Data>",
					movimento.getNumeroPedido() != null ?  movimento.getNumeroPedido() : "<Número>");
		
		setTitulo(titulo);
	}

	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String tituloRecibo) {
		this.titulo = tituloRecibo;
	}
}
