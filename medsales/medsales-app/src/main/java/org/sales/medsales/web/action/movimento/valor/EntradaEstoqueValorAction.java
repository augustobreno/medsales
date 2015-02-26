package org.sales.medsales.web.action.movimento.valor;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.valor.EntradaEstoqueValor;
import org.sales.medsales.negocio.movimentacao.valor.EntradaEstoqueValorFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaEstoqueValorAction extends MovimentoValorActionBase<EntradaEstoqueValor, EntradaEstoqueValorFacade>{
	
	
}
