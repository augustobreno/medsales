package org.sales.medsales.web.action.movimento.estoque;

import java.util.Date;
import java.util.LinkedList;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.Item;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;

/**
 * Para cadastro de uma Entrada de Produtos no estoque
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaAction extends CriarMovimentacaoBaseAction<EntradaEstoque> {

	private SaidaEstoque saidaGerada;

	@Override
	protected void initMovimentacao() {
		EntradaEstoque entradaEstoque = new EntradaEstoque();
		entradaEstoque.setItens(new LinkedList<Item>());
		entradaEstoque.setDataMovimento(new Date());
		setMovimentacao(entradaEstoque);
	}

	@Override
	protected void salvar() {
		getEstoqueFacade().salvar(getMovimentacao());
	}

	/**
	 * Conclui a entrada e gera uma saída com os mesmo itens.
	 */
	public void concluirGerarSaida() {
		concluir();
		gerarSaida();
	}
	
	/**
	 * Gera uma saída com os mesmo itens desta entrada.
	 */
	public void gerarSaida() {
		saidaGerada = getEstoqueFacade().gerarSaida(getMovimentacao().getId());
		showInfoMessage("A SAÍDA foi gerada a partir da Entrada Nº {0}", String.valueOf(getMovimentacao().getId()));
	}

	public SaidaEstoque getSaidaGerada() {
		return saidaGerada;
	}
	
}
