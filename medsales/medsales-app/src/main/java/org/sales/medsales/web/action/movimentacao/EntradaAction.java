package org.sales.medsales.web.action.movimentacao;

import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimentacao.Entrada;

/**
 * Para cadastro de uma Entrada de Produtos no estoque
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaAction extends CriarMovimentacaoBaseAction<Entrada> {

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

	
	
}
