package org.sales.medsales.web.action.movimento.estoque;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
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
	
}
