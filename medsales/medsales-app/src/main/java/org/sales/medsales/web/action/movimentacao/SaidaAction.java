package org.sales.medsales.web.action.movimentacao;

import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimentacao.Saida;

/**
 * Mantém o fluxo de cadastro e manutenção de saída (venda) de produtos.
 * 
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class SaidaAction extends CriarMovimentacaoBaseAction<Saida> {

	@Override
	protected void initMovimentacao() {
		Saida saida = new Saida();
		saida.setDataMovimentacao(new Date());
		setMovimentacao(saida);
	}
	
	@Override
	protected void salvar() {
		getEstoqueFacade().cadastrar(getMovimentacao());
	}
	
}
