package org.sales.medsales.web.action.movimento.valor;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.Operacao;
import org.sales.medsales.dominio.movimento.valor.Valor;
import org.sales.medsales.negocio.movimentacao.valor.ValorFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ValorAction extends MovimentoValorActionBase<Valor, ValorFacade>{
	
	/**
	 * @return Possíveis operações associadas a um valor.
	 */
	public Operacao[] getOperacoesValor() {
		return new Operacao[] {Operacao.ENTRADA, Operacao.SAIDA};
	}
	
}
