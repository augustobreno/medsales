package org.sales.medsales.web.action.movimentacao;

import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.Saida;

/**
 * Para cadastro de uma Entrada de Produtos no estoque
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaAction extends CriarMovimentacaoBaseAction<Entrada> {

	private Saida saidaGerada;

	@Override
	protected void initMovimentacao() {
		Entrada entrada = new Entrada();
		entrada.setDataMovimentacao(new Date());
		setMovimentacao(entrada);
	}

	@Override
	protected void salvar() {
		getEstoqueFacade().cadastrar(getMovimentacao());
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

	public Saida getSaidaGerada() {
		return saidaGerada;
	}
	
}
