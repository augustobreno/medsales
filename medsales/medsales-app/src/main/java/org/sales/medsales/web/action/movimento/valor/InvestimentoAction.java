package org.sales.medsales.web.action.movimento.valor;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.negocio.movimentacao.valor.InvestimentoFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class InvestimentoAction extends MovimentoValorActionBase<Investimento, InvestimentoFacade>{


}
